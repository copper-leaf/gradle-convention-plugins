@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import com.copperleaf.gradle.ConventionConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.application")
}

android {
    namespace = "com.copperleaf.${project.name.replace("-", ".")}"

    compileOptions {
        sourceCompatibility = ConventionConfig.repoInfo(project).javaVersionEnum
        targetCompatibility = ConventionConfig.repoInfo(project).javaVersionEnum
    }

    compileSdk = 35
    defaultConfig {
        minSdk = 21
        targetSdk = 35
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        getByName("debug") {
        }

        val publishConfiguration = ConventionConfig.publishConfig(project)
        if(publishConfiguration.androidReleaseModeEnabled) {
            create("release") {
                storeFile = file(publishConfiguration.androidKeystorePath)
                storePassword = publishConfiguration.androidKeystorePassword
                keyAlias = publishConfiguration.androidKeystoreKeyAlias
                keyPassword = publishConfiguration.androidKeystoreKeyPassword
            }
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        val publishConfiguration = ConventionConfig.publishConfig(project)
        if(publishConfiguration.androidReleaseModeEnabled) {
            release {
                // Releases are signed by CI/CD pipelines
                signingConfig = signingConfigs.getByName("release")

                // Enables code shrinking, obfuscation, and optimization for only
                // your project's release build type.
                isMinifyEnabled = true

                // Enables resource shrinking, which is performed by the
                // Android Gradle plugin.
                isShrinkResources = true

                // Includes the default ProGuard rules files that are packaged with
                // the Android Gradle plugin. To learn more, go to the section about
                // R8 configuration files.
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    lint {
        disable += listOf("GradleDependency")
    }
}

afterEvaluate {
    // Remove log pollution until Android support in KMP improves.
    project.extensions.findByType<KotlinMultiplatformExtension>()?.let { kmpExt ->
        kmpExt.sourceSets.removeAll {
            setOf(
                "androidAndroidTestRelease",
                "androidTestFixtures",
                "androidTestFixturesDebug",
                "androidTestFixturesRelease",
            ).contains(it.name)
        }
    }
}
