package com.copperleaf.gradle

import org.gradle.api.Project

data class SubprojectInfo(
    val description: String,
    val explicitApi: Boolean,
    val contextReceivers: Boolean,

    val kotlinAndroid: Boolean,
    val kotlinJvm: Boolean,
    val kotlinIos: Boolean,
    val kotlinJs: Boolean,
    val kotlinJsExecutable: Boolean,
    val kotlinWasmWasi: Boolean,
    val kotlinWasmJs: Boolean,
    val kotlinWasmJsExecutable: Boolean,

    val composeMaterial2: Boolean,
    val composeMaterial3: Boolean,
    val composeSplitPane: Boolean,
    val composeJsDom: Boolean,
) {
    companion object {
        fun get(project: Project): SubprojectInfo {
            val conventionProperties = ConventionProperties(project)

            return SubprojectInfo(
                description = conventionProperties.property("copperleaf.description"),
                explicitApi = conventionProperties.booleanProperty("copperleaf.explicitApi", defaultValue = true),
                contextReceivers = conventionProperties.booleanProperty("copperleaf.contextReceivers", defaultValue = false),

                kotlinAndroid = conventionProperties.booleanProperty("copperleaf.targets.android"),
                kotlinJvm = conventionProperties.booleanProperty("copperleaf.targets.jvm"),
                kotlinIos = conventionProperties.booleanProperty("copperleaf.targets.ios"),
                kotlinJs = conventionProperties.booleanProperty("copperleaf.targets.js"),
                kotlinJsExecutable = conventionProperties.booleanProperty("copperleaf.targets.js.executable"),
                kotlinWasmWasi = conventionProperties.booleanProperty("copperleaf.targets.wasm.wasi"),
                kotlinWasmJs = conventionProperties.booleanProperty("copperleaf.targets.wasm.js"),
                kotlinWasmJsExecutable = conventionProperties.booleanProperty("copperleaf.targets.wasm.js.executable"),

                composeMaterial2 = conventionProperties.booleanProperty("copperleaf.compose.material2", defaultValue = true),
                composeMaterial3 = conventionProperties.booleanProperty("copperleaf.compose.material3", defaultValue = false),
                composeSplitPane = conventionProperties.booleanProperty("copperleaf.compose.splitPane"),
                composeJsDom = conventionProperties.booleanProperty("copperleaf.compose.js.dom"),
            )
        }
    }
}
