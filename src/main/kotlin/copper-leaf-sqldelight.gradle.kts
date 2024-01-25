import com.copperleaf.gradle.ConventionConfig
import dev.icerock.gradle.MRVisibility

plugins {
    kotlin("multiplatform")
    id("app.cash.sqldelight")
}

kotlin {
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    sourceSets {
        val commonMain by getting {
            dependencies { }
        }

        if (subprojectInfo.kotlinAndroid) {
            val androidMain by getting {
                dependencies {
                    implementation("app.cash.sqldelight:android-driver:2.0.1")
                }
            }
        }

        if (subprojectInfo.kotlinJvm) {
            val jvmMain by getting {
                dependencies {
                    implementation("app.cash.sqldelight:sqlite-driver:2.0.1")
                }
            }
        }

        if (subprojectInfo.kotlinJs) {
            val jsMain by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinIos) {
            val iosMain by getting {
                dependencies {
                    implementation("app.cash.sqldelight:native-driver:2.0.1")
                }
            }
        }
    }
}
