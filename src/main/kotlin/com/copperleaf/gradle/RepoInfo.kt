package com.copperleaf.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

data class RepoInfo(
    val groupId: String,
    val githubUrl: String,
    val license: License,
    val javaVersion: String,
    val javaVersionEnum: JvmTarget,
    val developer: Developer,
) {
    companion object {
        fun get(project: Project): RepoInfo {
            return RepoInfo(
                groupId = "io.github.copper-leaf",
                githubUrl = "https://github.com/copper-leaf/${project.rootProject.name}",
                license = License.BSD3,
                javaVersion = JavaVersion.VERSION_17.toString(),
                javaVersionEnum = JvmTarget.JVM_17,
                developer = Developer(
                    id = "cjbrooks12",
                    name = "Casey Brooks",
                    email = "cjbrooks12@gmail.com",
                ),
            )
        }
    }
}
