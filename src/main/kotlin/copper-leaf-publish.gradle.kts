import com.copperleaf.gradle.ConventionConfig
import com.copperleaf.gradle.PublishConfiguration

plugins {
    `maven-publish`
    signing
}

// from https://kotlinlang.org/docs/multiplatform-library.html

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    val publishConfiguration: PublishConfiguration = ConventionConfig.publishConfig(project)

    // Configure maven central repository
    repositories {
        // publish to the project buildDir to make sure things are getting published correctly
        maven(url = "${project.layout.buildDirectory.asFile.get()}/.m2/repository") {
            name = "project"
        }
        maven(url = "${publishConfiguration.mavenRepositoryBaseUrl}/service/local/staging/deployByRepositoryId/${publishConfiguration.stagingRepositoryId}") {
            name = "mavenCentral"
            credentials {
                username = publishConfiguration.ossrhUsername
                password = publishConfiguration.ossrhPassword
            }
        }
        maven(url = "${publishConfiguration.mavenRepositoryBaseUrl}/content/repositories/snapshots/") {
            name = "mavenCentralSnapshots"
            credentials {
                username = publishConfiguration.ossrhUsername
                password = publishConfiguration.ossrhPassword
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {
        val conventionConfig = ConventionConfig.repoInfo(project)

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set(project.name)
            description.set("${project.description}")
            url.set(conventionConfig.githubUrl)

            licenses {
                license {
                    name.set(conventionConfig.license.spdxIdentifier)
                    url.set(conventionConfig.license.url)
                }
            }
            developers {
                developer {
                    id.set(conventionConfig.developer.id)
                    name.set(conventionConfig.developer.name)
                    email.set(conventionConfig.developer.email)
                }
            }
            scm {
                url.set("${conventionConfig.githubUrl}.git")
            }
        }
    }
}

signing {
    val publishConfiguration: PublishConfiguration = ConventionConfig.publishConfig(project)

    useInMemoryPgpKeys(
        publishConfiguration.signingKeyId,
        publishConfiguration.signingKey,
        publishConfiguration.signingPassword
    )
    sign(publishing.publications)
}

afterEvaluate {
    val hostInfo = ConventionConfig.hostInfo(project)
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    // Fix Gradle warning about signing tasks using publishing task outputs without explicit dependencies
    // https://youtrack.jetbrains.com/issue/KT-46466
    tasks.withType<AbstractPublishToMaven>().configureEach {
        val signingTasks = tasks.withType<Sign>()
        mustRunAfter(signingTasks)
    }

    if (subprojectInfo.kotlinIos && hostInfo.isMac) {
        tasks.named("compileTestKotlinIosX64").configure { mustRunAfter("signIosX64Publication") }
        tasks.named("compileTestKotlinIosSimulatorArm64").configure { mustRunAfter("signIosSimulatorArm64Publication") }
        tasks.named("linkDebugTestIosSimulatorArm64").configure { mustRunAfter("signIosSimulatorArm64Publication") }
    }
}
