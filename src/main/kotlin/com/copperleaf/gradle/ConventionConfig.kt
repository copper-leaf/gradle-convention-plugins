package com.copperleaf.gradle

import org.gradle.api.Project
import org.gradle.internal.impldep.org.junit.platform.commons.util.Preconditions.condition
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate

object ConventionConfig {

    /**
     * Gets info about the host OS that this Gradle project is currently running on.
     */
    fun hostInfo(project: Project): HostInfo {
        return HostInfo.get(project)
    }

    /**
     * Gets info common to everything in this repo. Mostly used for publishing.
     */
    fun repoInfo(project: Project): RepoInfo {
        return RepoInfo.get(project)
    }

    /**
     * Gets info for a single subproject in this repo. Mostly used for configuring Kotlin targets.
     */
    fun subprojectInfo(project: Project): SubprojectInfo {
        return SubprojectInfo.get(project)
    }

    /**
     * Gets the project version. This property should be the same among all subprojects, and
     * is based on Git tags and messages, so the actual value is cached in the project extras.
     */
    fun projectVersion(project: Project): ProjectVersion {
        val projectVersion: ProjectVersion by project.extra
        return projectVersion
    }
}
