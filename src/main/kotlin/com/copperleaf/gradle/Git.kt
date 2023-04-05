package com.copperleaf.gradle

import org.apache.tools.ant.taskdefs.Execute
import org.gradle.api.Project
import java.io.ByteArrayOutputStream

class Git(project: Project) : Shell(project) {

    fun getCurrentSha(): String {
        return runCommand("git rev-parse HEAD")
    }

    fun getLatestTagSha(): String {
        return runCommand("git rev-list --tags --max-count=1")
    }

    fun getLatestTagName(): String {
        return runCommand("git describe --abbrev=0 --tags")
    }

    fun getCommitsSinceLastTag(latestTagName: String): List<String> {
        return runCommand("git log ${latestTagName}..HEAD --oneline --pretty=format:%s").lines().reversed()
    }

    fun hasUncommittedChanges(): Boolean {
        return runCommand("git status --porcelain").isBlank()
    }
}
