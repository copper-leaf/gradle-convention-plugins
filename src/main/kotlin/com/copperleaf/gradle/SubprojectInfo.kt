package com.copperleaf.gradle

import org.gradle.api.Project

data class SubprojectInfo(
    val description: String,
    val explicitApi: Boolean,
    val kotlinAndroid: Boolean,
    val kotlinJvm: Boolean,
    val kotlinIos: Boolean,
    val kotlinJs: Boolean,
) {
    companion object {
        fun get(project: Project): SubprojectInfo {
            val conventionProperties = ConventionProperties(project)

            return SubprojectInfo(
                description = conventionProperties.gradleProperty("copperleaf.description"),
                explicitApi = conventionProperties.booleanGradleProperty("copperleaf.explicitApi", defaultValue = true),
                kotlinAndroid = conventionProperties.booleanGradleProperty("copperleaf.targets.android"),
                kotlinJvm = conventionProperties.booleanGradleProperty("copperleaf.targets.jvm"),
                kotlinIos = conventionProperties.booleanGradleProperty("copperleaf.targets.ios"),
                kotlinJs = conventionProperties.booleanGradleProperty("copperleaf.targets.js"),
            )
        }
    }
}
