package com.copperleaf.gradle

import org.gradle.api.Project
import java.io.File
import java.util.*


data class PublishConfiguration(
    val githubUser: String,
    val githubToken: String,

    val stagingRepositoryIdFile: File,
    val stagingRepositoryIdEnvName: String,
    val stagingProfileId: String,

    val signingKeyId: String,
    val signingKey: String,
    val signingPassword: String,
    val sonatypeUsername: String,
    val sonatypePassword: String,

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
                System.getenv(stagingRepositoryIdEnvName)
                    .takeIf { !it.isNullOrBlank() }
                    ?: ""
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
        return debug(bypassSecurity = false)
    }

    fun debug(bypassSecurity: Boolean): String {
        fun printValue(isSecure: Boolean, value: String?): String {
            return if (!isSecure || bypassSecurity) {
                if (value.isNullOrBlank()) "" else value
            } else {
                if (value.isNullOrBlank()) "" else "[REDACTED]"
            }
        }

        return """
            |PublishConfiguration(
            |    githubUser=${printValue(true, githubUser)}
            |    githubToken=${printValue(true, githubToken)}
            |    stagingRepositoryIdFile=${printValue(false, stagingRepositoryIdFile.toString())}
            |    stagingRepositoryId=${printValue(false, stagingRepositoryId)}
            |    stagingProfileId=${printValue(false, stagingProfileId)}
            |
            |    signingKeyId=${printValue(true, signingKeyId)}
            |    signingKey=${printValue(true, signingKey)}
            |    signingPassword=${printValue(true, signingPassword)}
            |    sonatypeUsername=${printValue(true, sonatypeUsername)}
            |    sonatypePassword=${printValue(true, sonatypePassword)}
            |    
            |    jetbrainsMarketplacePassphrase=${printValue(true, jetbrainsMarketplacePassphrase)}
            |    jetbrainsMarketplacePrivateKey=${printValue(true, jetbrainsMarketplacePrivateKey)}
            |    jetbrainsMarketplaceCertificateChain=${printValue(true, jetbrainsMarketplaceCertificateChain)}
            |    jetbrainsMarketplaceToken=${printValue(true, jetbrainsMarketplaceToken)}
            |    
            |    androidKeystoreEncodedText=${printValue(true, androidKeystoreEncodedText)}
            |    androidKeystorePath=${printValue(true, androidKeystorePath)}
            |    androidKeystorePassword=${printValue(true, androidKeystorePassword)}
            |    androidKeystoreKeyAlias=${printValue(true, androidKeystoreKeyAlias)}
            |    androidKeystoreKeyPassword=${printValue(true, androidKeystoreKeyPassword)}
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

                stagingRepositoryIdFile = project.rootProject.layout.buildDirectory.asFile.get().resolve("export")
                    .resolve("stagingRepositoryId"),
                stagingRepositoryIdEnvName = "stagingRepositoryId",
                stagingProfileId = conventionProperties.property("staging_profile_id"),

                signingKeyId = conventionProperties.property("signing_key_id"),
                signingKey = conventionProperties.propertyAsFile("signing_key"),
                signingPassword = conventionProperties.property("signing_password"),
                sonatypeUsername = conventionProperties.property("sonatype_username"),
                sonatypePassword = conventionProperties.property("sonatype_password"),

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
            ).also {
                it.saveKeystoreFile(project)
            }
        }
    }
}
