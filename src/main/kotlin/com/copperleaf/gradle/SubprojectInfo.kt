package com.copperleaf.gradle

import org.gradle.api.Project

data class SubprojectInfo(
    val description: String,
    val explicitApi: Boolean,

    val kotlinAndroid: Boolean,
    val kotlinJvm: Boolean,
    val kotlinJvmWithJava: Boolean,
    val kotlinIos: Boolean,
    val kotlinJs: Boolean,
    val kotlinJsExecutable: Boolean,

    val composeMaterial2: Boolean,
    val composeSplitPane: Boolean,
    val composeJsDom: Boolean,
) {
    companion object {
        fun get(project: Project): SubprojectInfo {
            val conventionProperties = ConventionProperties(project)

            return SubprojectInfo(
                description = conventionProperties.gradleProperty("copperleaf.description"),
                explicitApi = conventionProperties.booleanGradleProperty("copperleaf.explicitApi", defaultValue = true),

                kotlinAndroid = conventionProperties.booleanGradleProperty("copperleaf.targets.android"),
                kotlinJvm = conventionProperties.booleanGradleProperty("copperleaf.targets.jvm"),
                kotlinJvmWithJava = conventionProperties.booleanGradleProperty("copperleaf.targets.jvm.withJava"),
                kotlinIos = conventionProperties.booleanGradleProperty("copperleaf.targets.ios"),
                kotlinJs = conventionProperties.booleanGradleProperty("copperleaf.targets.js"),
                kotlinJsExecutable = conventionProperties.booleanGradleProperty("copperleaf.targets.js.executable"),

                composeMaterial2 = conventionProperties.booleanGradleProperty("copperleaf.compose.material2", defaultValue = true),
                composeSplitPane = conventionProperties.booleanGradleProperty("copperleaf.compose.splitPane"),
                composeJsDom = conventionProperties.booleanGradleProperty("copperleaf.compose.js.dom"),
            )
        }
    }
}
