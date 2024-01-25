package com.copperleaf.gradle

import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.gradle.kotlin.dsl.invoke

fun KotlinMultiplatformExtension.nativeTargetGroup(
    name: String,
    vararg targets: KotlinNativeTarget
): Array<out KotlinNativeTarget> {
    sourceSets {
        if (targets.size > 1) {
            val main = create("${name}Main") {
            }
            val test = create("${name}Test") {
            }
            main to test
        }
    }

    return targets
}
