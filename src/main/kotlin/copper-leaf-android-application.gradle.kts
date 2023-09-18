@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import com.copperleaf.gradle.ConventionConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.application")
}

android {
    namespace = "com.copperleaf.${project.name.replace("-", ".")}"
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        targetSdk = 34
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        getByName("debug") {
        }
        create("release") {
            val publishConfiguration = ConventionConfig.publishConfig(project)
            storeFile = file(publishConfiguration.androidKeystorePath)
            storePassword = publishConfiguration.androidKeystorePassword
            keyAlias = publishConfiguration.androidKeystoreKeyAlias
            keyPassword = publishConfiguration.androidKeystoreKeyPassword
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

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
