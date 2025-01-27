@file:Suppress("UNUSED_VARIABLE")

import com.copperleaf.gradle.ConventionConfig

plugins {
    kotlin("multiplatform")
}

kotlin {
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
            }
        }

        if (subprojectInfo.kotlinAndroid) {
            val androidUnitTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinJvm) {
            val jvmTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinJs) {
            val jsTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinIos) {
            val iosTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinWasmWasi) {
            val wasmWasiTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinWasmJs) {
            val wasmJsTest by getting {
                dependencies { }
            }
        }
    }
}

tasks.withType<Test> {
//    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
