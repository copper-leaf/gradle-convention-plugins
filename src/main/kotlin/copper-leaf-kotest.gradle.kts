@file:Suppress("UNUSED_VARIABLE")

import com.copperleaf.gradle.ConventionConfig

plugins {
    kotlin("multiplatform")
    id("io.kotest.multiplatform")
}

kotlin {
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

                implementation("io.kotest:kotest-framework-engine:5.6.2")
                implementation("io.kotest:kotest-assertions-core:5.6.2")
                implementation("io.kotest:kotest-framework-datatest:5.6.2")
                implementation("io.kotest:kotest-property:5.6.2")
            }
        }

        if (subprojectInfo.kotlinAndroid) {
            val androidUnitTest by getting {
                dependencies {
                    implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                    implementation("io.kotest:kotest-runner-junit5:5.6.2")
                }
            }
        }

        if (subprojectInfo.kotlinJvm) {
            val jvmTest by getting {
                dependencies {
                    implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                    implementation("io.kotest:kotest-runner-junit5:5.6.2")
                }
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
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
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
