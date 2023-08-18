package com.copperleaf.gradle

import org.gradle.api.Project
import java.util.*

class ConventionProperties(private val project: Project) {

    fun gradleProperty(
        projectPropertyName: String,
    ): String {
        return gradleProjectProperty(projectPropertyName)
            ?: ""
    }

    fun booleanGradleProperty(
        projectPropertyName: String,
        defaultValue: Boolean = false,
    ): Boolean {
        return gradleProjectProperty(projectPropertyName)
            ?.toBoolean()
            ?: defaultValue
    }

    fun envOrGradleProperty(
        projectPropertyName: String,
        envName: String = projectPropertyName.uppercase(Locale.getDefault())
    ): String {
        return env(envName)
            ?: gradleProjectProperty(projectPropertyName)
            ?: ""
    }

    fun envOrGradlePropertyAsFile(
        projectPropertyName: String,
        envName: String = projectPropertyName.uppercase(Locale.getDefault())
    ): String {
        return decodeIfNeeded(env(envName))
            ?: decodeIfNeeded(gradleProjectProperty(projectPropertyName))
            ?: ""
    }

// Utils
// ---------------------------------------------------------------------------------------------------------------------

    private fun gradleProjectProperty(
        projectPropertyName: String,
    ): String? {
        val projectPropertiesValue = project.properties[projectPropertyName]?.toString()
        if (projectPropertiesValue != null) return projectPropertiesValue

        return null
    }

    fun env(
        envName: String,
    ): String? {
        val envValue = System.getenv(envName)?.toString()
        if (envValue != null) return envValue

        return null
    }

    /**
     * resolvePropertyAsFileAndLoadContentsIfNeeded
     */
    private fun decodeIfNeeded(propertyValue: String?): String? {
        return if(propertyValue == null) {
            null
        } else if (propertyValue.startsWith("~/")) {
            // the value is a path to file on disk. Read its contents
            val filePath = propertyValue.replace("~/", System.getProperty("user.home") + "/")
            project.file(filePath).readText()
        } else {
            // the value itself is the file contents file
            propertyValue
        }
    }
}
