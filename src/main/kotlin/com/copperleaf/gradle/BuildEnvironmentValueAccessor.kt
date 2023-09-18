package com.copperleaf.gradle

import org.gradle.api.Project
import java.io.File
import java.util.*

internal sealed interface BuildEnvironmentValueAccessor {

    abstract fun getPropertyOrNull(name: String): String?
}

internal class LocalPropertiesAccessor(
    private val project: Project,
    localPropertiesFile: File,
) : BuildEnvironmentValueAccessor {
    private val localProperties: Properties by lazy {
        Properties()
            .apply {
                if (localPropertiesFile.exists()) {
                    load(localPropertiesFile.bufferedReader())
                }
            }
    }

    override fun getPropertyOrNull(name: String): String? {
        return localProperties.getProperty(name, null)
    }
}

internal class GradleProjectPropertiesAccessor(
    private val project: Project
) : BuildEnvironmentValueAccessor {
    override fun getPropertyOrNull(name: String): String? {
        return project.properties[name]?.toString()
    }
}

internal class EnvironmentVariablesAccessor(
    private val project: Project
) : BuildEnvironmentValueAccessor {
    override fun getPropertyOrNull(name: String): String? {
        val envName = when (name) {
            "github_username" -> "GITHUB_ACTOR"
            "github_token" -> "GITHUB_TOKEN"
            else -> name.uppercase(Locale.getDefault())
        }

        return System.getenv(envName)?.toString()
    }
}

internal class DecodeAsFileAccessor(
    private val project: Project,
    private val delegate: BuildEnvironmentValueAccessor
) : BuildEnvironmentValueAccessor {
    override fun getPropertyOrNull(name: String): String? {
        val propertyValue = delegate.getPropertyOrNull(name)

        return if (propertyValue == null) {
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

internal class DelegatedAccessor(
    private val project: Project,
    private vararg val delegates: BuildEnvironmentValueAccessor
) : BuildEnvironmentValueAccessor {
    override fun getPropertyOrNull(name: String): String? {
        return delegates
            .firstNotNullOfOrNull { it.getPropertyOrNull(name) }
    }
}
