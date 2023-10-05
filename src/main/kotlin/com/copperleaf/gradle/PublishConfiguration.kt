package com.copperleaf.gradle

import org.gradle.api.Project
import java.io.File
import java.util.*


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

    val androidKeystoreEncodedText: String,
    val androidKeystorePath: String,
    val androidKeystorePassword: String,
    val androidKeystoreKeyAlias: String,
    val androidKeystoreKeyPassword: String,
    val androidReleaseModeEnabled: Boolean,
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
            |    
            |    androidKeystoreEncodedText=${if (androidKeystoreEncodedText.isNotBlank()) "[REDACTED]" else ""}
            |    androidKeystorePath=${if (androidKeystorePath.isNotBlank()) "[REDACTED]" else ""}
            |    androidKeystorePassword=${if (androidKeystorePassword.isNotBlank()) "[REDACTED]" else ""}
            |    androidKeystoreKeyAlias=${if (androidKeystoreKeyAlias.isNotBlank()) "[REDACTED]" else ""}
            |    androidKeystoreKeyPassword=${if (androidKeystoreKeyPassword.isNotBlank()) "[REDACTED]" else ""}
            |)
        """.trimMargin()
    }

    fun saveKeystoreFile(project: Project) {
        // don't need to do anything if the keystore is already there
        // during (local dev) or isn't specified (not running an
        // Android app)
        val keystoreFile = project.file(androidKeystorePath)
        if (keystoreFile.exists()) return
        if (androidKeystoreEncodedText.isBlank()) return

        val data: ByteArray = Base64.getDecoder().decode(androidKeystoreEncodedText)
        keystoreFile.createNewFile()
        keystoreFile.outputStream().use { stream -> stream.write(data) }
    }

    companion object {
        fun get(project: Project): PublishConfiguration {
            val conventionProperties = ConventionProperties(project)

            return PublishConfiguration(
                githubUser = conventionProperties.property("github_username"),
                githubToken = conventionProperties.property("github_token"),

                mavenRepositoryBaseUrl = "https://s01.oss.sonatype.org",
                stagingRepositoryIdFile = project.rootProject.layout.buildDirectory.asFile.get().resolve("export")
                    .resolve("stagingRepositoryId"),
                stagingProfileId = conventionProperties.property("staging_profile_id"),

                signingKeyId = conventionProperties.property("signing_key_id"),
                signingKey = conventionProperties.propertyAsFile("signing_key"),
                signingPassword = conventionProperties.property("signing_password"),
                ossrhUsername = conventionProperties.property("ossrh_username"),
                ossrhPassword = conventionProperties.property("ossrh_password"),

                jetbrainsMarketplacePassphrase = conventionProperties.property("jb_passphrase"),
                jetbrainsMarketplacePrivateKey = conventionProperties.propertyAsFile("jb_signing_key"),
                jetbrainsMarketplaceCertificateChain = conventionProperties.propertyAsFile("jb_chain"),
                jetbrainsMarketplaceToken = conventionProperties.property("jb_marketplace_token"),

                androidKeystoreEncodedText = conventionProperties.property("keystore_encoded"),
                androidKeystorePath = conventionProperties.property("keystore_path", "./../release.keystore"),
                androidKeystorePassword = conventionProperties.property("keystore_password"),
                androidKeystoreKeyAlias = conventionProperties.property("keystore_key_alias"),
                androidKeystoreKeyPassword = conventionProperties.property("keystore_key_password"),
                androidReleaseModeEnabled = conventionProperties.booleanProperty("copperleaf.targets.android.release.enabled", true),
            ).also { it.saveKeystoreFile(project) }
        }
    }
}
