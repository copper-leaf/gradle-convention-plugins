@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import gradle.kotlin.dsl.accessors._9c04ceaef20eeb51aed3ad37a03c872b.publishing
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.library")
}

android {
    namespace = "com.copperleaf.${project.name.replace("-", ".")}"

    compileSdk = 35
    defaultConfig {
        minSdk = 21
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
    publishing {
        singleVariant("debug") {
            withSourcesJar()
            withJavadocJar()
        }
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("androidDebug") {
                from(components["debug"])
            }
            register<MavenPublication>("androidRelease") {
                from(components["release"])
            }
        }
    }

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
