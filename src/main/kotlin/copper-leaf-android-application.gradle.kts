@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.application")
}

android {
    namespace = "com.copperleaf.${project.name.replace("-", ".")}"
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
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
