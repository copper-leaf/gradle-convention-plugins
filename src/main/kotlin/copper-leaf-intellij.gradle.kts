@file:Suppress("OPT_IN_IS_NOT_ENABLED")
@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

import com.copperleaf.gradle.ConventionConfig

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.intellij")
    id("org.jetbrains.changelog")
}

dependencies {
    // compose Desktop Intellij Plugin
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.desktop.components.splitPane)
    implementation(compose.materialIconsExtended)

    // Ktor websocket server
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    type.set("IC")
    version.set("2023.1.1")
    downloadSources.set(true)
    plugins.set(listOf("org.jetbrains.kotlin"))
    updateSinceUntilBuild.set(false)
}

tasks {
    buildSearchableOptions {
        // temporary workaround
        enabled = false
    }

    signPlugin {
        val publishConfiguration = ConventionConfig.publishConfig(project)
        certificateChain.set(publishConfiguration.jetbrainsMarketplaceCertificateChain)
        privateKey.set(publishConfiguration.jetbrainsMarketplacePrivateKey)
        password.set(publishConfiguration.jetbrainsMarketplacePassphrase)
    }

    publishPlugin {
        val publishConfiguration = ConventionConfig.publishConfig(project)
        token.set(publishConfiguration.jetbrainsMarketplaceToken)
    }

    runPluginVerifier {
        ideVersions.set(
            listOf(
                "2020.3.2",
                "2021.1",
                "2022.1",
                "2022.1.4",
                "2022.3",
                "2023.1.1",
            )
        )
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.ExperimentalStdlibApi")
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
        freeCompilerArgs.add("-opt-in=androidx.compose.foundation.ExperimentalFoundationApi")
        freeCompilerArgs.add("-opt-in=androidx.compose.animation.ExperimentalAnimationApi")
        freeCompilerArgs.add("-opt-in=androidx.compose.ui.ExperimentalComposeUiApi")
        freeCompilerArgs.add("-opt-in=androidx.compose.material.ExperimentalMaterialApi")
        freeCompilerArgs.add("-opt-in=org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi")
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}
