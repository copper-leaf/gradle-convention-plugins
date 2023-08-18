package com.copperleaf.gradle

import org.gradle.api.Project
import java.io.File

data class PublishConfiguration(
    val githubUser: String,
    val githubToken: String,

    val mavenRepositoryBaseUrl: String,
    val stagingRepositoryIdFile: File,
    val stagingProfileId: String,

    val signingKeyId: String,
    val signingKey: String,
    val signingPassword: String,
    val ossrhUsername: String,
    val ossrhPassword: String,

    val jetbrainsMarketplacePassphrase: String,
    val jetbrainsMarketplacePrivateKey: String,
    val jetbrainsMarketplaceCertificateChain: String,
    val jetbrainsMarketplaceToken: String,
) {
    var stagingRepositoryId: String
        get() {
            return if (stagingRepositoryIdFile.exists()) {
                stagingRepositoryIdFile.readText()
            } else {
                ""
            }
        }
        set(value) {
            if (stagingRepositoryIdFile.exists()) {
                stagingRepositoryIdFile.delete()
            }

            stagingRepositoryIdFile.parentFile.mkdirs()
            stagingRepositoryIdFile.createNewFile()

            stagingRepositoryIdFile.writeText(value)
        }

    override fun toString(): String {
        return debug()
    }

    fun debug(): String {
        return """
            |PublishConfiguration(
            |    githubUser=${if (githubUser.isNotBlank()) "[REDACTED]" else ""}
            |    githubToken=${if (githubToken.isNotBlank()) "[REDACTED]" else ""}
            |    mavenRepositoryBaseUrl=$mavenRepositoryBaseUrl
            |    stagingRepositoryIdFile=$stagingRepositoryIdFile
            |    stagingRepositoryId=$stagingRepositoryId
            |    stagingProfileId=$stagingProfileId
            |
            |    signingKeyId=${if (signingKeyId.isNotBlank()) "[REDACTED]" else ""}
            |    signingKey=${if (signingKey.isNotBlank()) "[REDACTED]" else ""}
            |    signingPassword=${if (signingPassword.isNotBlank()) "[REDACTED]" else ""}
            |    ossrhUsername=${if (ossrhUsername.isNotBlank()) "[REDACTED]" else ""}
            |    ossrhPassword=${if (ossrhPassword.isNotBlank()) "[REDACTED]" else ""}
            |    
            |    jetbrainsMarketplacePassphrase=${if (jetbrainsMarketplacePassphrase.isNotBlank()) "[REDACTED]" else ""}
            |    jetbrainsMarketplacePrivateKey=${if (jetbrainsMarketplacePrivateKey.isNotBlank()) "[REDACTED]" else ""}
            |    jetbrainsMarketplaceCertificateChain=${if (jetbrainsMarketplaceCertificateChain.isNotBlank()) "[REDACTED]" else ""}
            |    jetbrainsMarketplaceToken=${if (jetbrainsMarketplaceToken.isNotBlank()) "[REDACTED]" else ""}
            |)
        """.trimMargin()
    }

    companion object {
        fun get(project: Project): PublishConfiguration {
            val conventionProperties = ConventionProperties(project)

            return PublishConfiguration(
                githubUser = conventionProperties.envOrGradleProperty("github_username", "GITHUB_ACTOR"),
                githubToken = conventionProperties.envOrGradleProperty("github_token", "GITHUB_TOKEN"),

                mavenRepositoryBaseUrl = "https://s01.oss.sonatype.org",
                stagingRepositoryIdFile = project.rootProject.layout.buildDirectory.asFile.get().resolve("export").resolve("stagingRepositoryId"),
                stagingProfileId = conventionProperties.envOrGradleProperty("staging_profile_id"),

                signingKeyId = conventionProperties.envOrGradleProperty("signing_key_id"),
                signingKey = conventionProperties.envOrGradlePropertyAsFile("signing_key"),
                signingPassword = conventionProperties.envOrGradleProperty("signing_password"),
                ossrhUsername = conventionProperties.envOrGradleProperty("ossrh_username"),
                ossrhPassword = conventionProperties.envOrGradleProperty("ossrh_password"),

                jetbrainsMarketplacePassphrase = conventionProperties.envOrGradleProperty("jb_passphrase"),
                jetbrainsMarketplacePrivateKey = conventionProperties.envOrGradlePropertyAsFile("jb_signing_key"),
                jetbrainsMarketplaceCertificateChain = conventionProperties.envOrGradlePropertyAsFile("jb_chain"),
                jetbrainsMarketplaceToken = conventionProperties.envOrGradleProperty("jb_marketplace_token"),
            )
        }
    }
}
