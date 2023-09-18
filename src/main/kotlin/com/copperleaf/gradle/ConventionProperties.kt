package com.copperleaf.gradle

import org.gradle.api.Project

class ConventionProperties(
    private val project: Project,
) {
    private val valueAccessor = DelegatedAccessor(
        project,
        LocalPropertiesAccessor(project, project.file("local.properties")),
        LocalPropertiesAccessor(project, project.rootProject.file("local.properties")),
        EnvironmentVariablesAccessor(project),
        GradleProjectPropertiesAccessor(project),
    )
    private val decodedEnvOrGradleAccessor = DecodeAsFileAccessor(
        project,
        valueAccessor
    )

    fun property(
        projectPropertyName: String,
        defaultValue: String = "",
    ): String {
        return valueAccessor.getPropertyOrNull(projectPropertyName)
            ?: defaultValue
    }

    fun booleanProperty(
        projectPropertyName: String,
        defaultValue: Boolean = false,
    ): Boolean {
        return valueAccessor.getPropertyOrNull(projectPropertyName)
            ?.toBoolean()
            ?: defaultValue
    }

    fun propertyAsFile(
        projectPropertyName: String,
    ): String {
        return decodedEnvOrGradleAccessor.getPropertyOrNull(projectPropertyName)
            ?: ""
    }
}
