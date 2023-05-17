import com.copperleaf.gradle.ConventionConfig
import dev.icerock.gradle.MRVisibility

plugins {
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
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
                    implementation("com.squareup.sqldelight:android-driver:1.5.4")
                }
            }
        }

        if (subprojectInfo.kotlinJvm) {
            val jvmMain by getting {
                dependencies {
                    implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
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
                    implementation("com.squareup.sqldelight:native-driver:1.5.4")
                }
            }
        }
    }
}
