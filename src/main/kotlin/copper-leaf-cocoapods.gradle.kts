@file:Suppress("UNUSED_VARIABLE")

import com.copperleaf.gradle.ConventionConfig
import com.copperleaf.gradle.format
import com.copperleaf.gradle.nativeTargetGroup
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    val projectVersion = ConventionConfig.projectVersion(project)
    val subprojectInfo = ConventionConfig.subprojectInfo(project)

    if (subprojectInfo.kotlinIos) {
        cocoapods {
            version = projectVersion.nextVersion.format(false)
            summary = subprojectInfo.description
            homepage = "empty"
            ios.deploymentTarget = "11.0"
            podfile = project.file("../iosApp/Podfile")
            framework {
                baseName = project.name
                isStatic = true
            }
        }
    } else {
        error("Cocoapods must have iOS configured")
    }
}
