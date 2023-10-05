@file:Suppress("OPT_IN_IS_NOT_ENABLED")
@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

import com.copperleaf.gradle.ConventionConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)

                if(subprojectInfo.composeMaterial2) {
                    implementation(compose.foundation)
                    implementation(compose.material)
                }
                if(subprojectInfo.composeMaterial3) {
                    implementation(compose.foundation)
                    implementation(compose.material3)
                }
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        if (subprojectInfo.kotlinAndroid) {
            val androidMain by getting {
                dependencies { }
            }
            val androidUnitTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinJvm) {
            val jvmMain by getting {
                dependencies {
                    implementation(compose.desktop.currentOs)

                    if(subprojectInfo.composeSplitPane) {
                        implementation(compose.desktop.components.splitPane)
                    }
                }
            }
            val jvmTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinJs) {
            val jsMain by getting {
                dependencies {
                    if(subprojectInfo.composeJsDom) {
                        implementation(compose.html.core)
                        implementation(compose.runtime)
                    }
                }
            }
            val jsTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinIos) {
            val iosMain by getting {
                dependencies { }
            }
            val iosTest by getting {
                dependencies { }
            }
        }
    }
}
