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

afterEvaluate {
    val hostInfo = ConventionConfig.hostInfo(project)
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    if (subprojectInfo.kotlinAndroid) {
        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }

        tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
        tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }
        tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }
        tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }

        if(subprojectInfo.kotlinJvm) {
            tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signJvmPublication") }
            tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signJvmPublication") }
        }
        if(subprojectInfo.kotlinIos && hostInfo.isMac) {
            tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signIosArm64Publication") }
            tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signIosArm64Publication") }
            tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
            tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
            tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signIosX64Publication") }
            tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signIosX64Publication") }
        }
        if(subprojectInfo.kotlinJs) {
            tasks.getByName("publishAndroidReleasePublicationToMavenLocal") { dependsOn("signJsPublication") }
            tasks.getByName("publishAndroidDebugPublicationToMavenLocal") { dependsOn("signJsPublication") }
        }
    }

    if (subprojectInfo.kotlinJvm) {
        tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }

        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signJvmPublication") }

        if(subprojectInfo.kotlinAndroid) {
            tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
            tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }
        }
        if(subprojectInfo.kotlinIos && hostInfo.isMac) {
            tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signIosX64Publication") }
            tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
            tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signIosArm64Publication") }
        }
        if(subprojectInfo.kotlinJs) {
            tasks.getByName("publishJvmPublicationToMavenLocal") { dependsOn("signJsPublication") }
        }
    }

    if (subprojectInfo.kotlinIos && hostInfo.isMac) {
        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signIosX64Publication") }
        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signIosArm64Publication") }

        tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
        tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signIosArm64Publication") }
        tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signIosX64Publication") }
        tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signIosX64Publication") }
        tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
        tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signIosArm64Publication") }
        tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }
        tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }
        tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }

        if (subprojectInfo.kotlinAndroid) {
            tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
            tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }
            tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
            tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }
            tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
            tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }
        }
        if (subprojectInfo.kotlinJvm) {
            tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signJvmPublication") }
            tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signJvmPublication") }
            tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signJvmPublication") }
        }
        if (subprojectInfo.kotlinJs) {
            tasks.getByName("publishIosX64PublicationToMavenLocal") { dependsOn("signJsPublication") }
            tasks.getByName("publishIosSimulatorArm64PublicationToMavenLocal") { dependsOn("signJsPublication") }
            tasks.getByName("publishIosArm64PublicationToMavenLocal") { dependsOn("signJsPublication") }
        }
    }

    if (subprojectInfo.kotlinJs) {
        tasks.getByName("publishKotlinMultiplatformPublicationToMavenLocal") { dependsOn("signJsPublication") }

        tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signKotlinMultiplatformPublication") }

        if(subprojectInfo.kotlinAndroid) {
            tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signAndroidReleasePublication") }
            tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signAndroidDebugPublication") }
        }
        if(subprojectInfo.kotlinJvm) {
            tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signJvmPublication") }
        }
        if(subprojectInfo.kotlinIos && hostInfo.isMac) {
            tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signIosX64Publication") }
            tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signIosSimulatorArm64Publication") }
            tasks.getByName("publishJsPublicationToMavenLocal") { dependsOn("signIosArm64Publication") }
        }
    }
}


/*









 */
