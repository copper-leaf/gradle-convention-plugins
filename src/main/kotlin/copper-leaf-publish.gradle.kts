import com.copperleaf.gradle.ConventionConfig
import com.copperleaf.gradle.PublishConfiguration

plugins {
    `maven-publish`
    signing
}

// MavenCentral Signing and Publishing
// ---------------------------------------------------------------------------------------------------------------------

// taken and modified from https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    val publishConfiguration: PublishConfiguration = ConventionConfig.repoInfo(project).publishConfiguration

    // Configure maven central repository
    repositories {
        // publish to the project buildDir to make sure things are getting published correctly
        maven(url = "${project.buildDir}/.m2/repository") {
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
    val publishConfiguration: PublishConfiguration = ConventionConfig.repoInfo(project).publishConfiguration

    useInMemoryPgpKeys(
        publishConfiguration.signingKeyId,
        publishConfiguration.signingKey,
        publishConfiguration.signingPassword
    )
    sign(publishing.publications)
}
