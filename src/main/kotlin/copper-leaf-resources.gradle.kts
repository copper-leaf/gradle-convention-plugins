import com.copperleaf.gradle.ConventionConfig
import dev.icerock.gradle.MRVisibility
import gradle.kotlin.dsl.accessors._d6ab92d9865bfb93fb5c469c9305c216.multiplatformResources

plugins {
    kotlin("multiplatform")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.icerock.moko:resources:0.23.0")
            }
        }
    }
}

val subprojectInfo = ConventionConfig.subprojectInfo(project)
multiplatformResources {
    multiplatformResourcesPackage = "com.copperleaf.${project.name}"
    iosBaseLocalizationRegion = "en"
}

