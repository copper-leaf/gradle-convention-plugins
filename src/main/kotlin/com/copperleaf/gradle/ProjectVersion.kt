package com.copperleaf.gradle

import net.swiftzer.semver.SemVer
import org.gradle.api.Project

data class ProjectVersion(
    val previousVersion: SemVer?,
    val nextVersion: SemVer,

    val isRelease: Boolean,
    val isDocsUpdate: Boolean,

    val snapshotSuffix: String,
    val previousSha: String,
    val latestSha: String,
    val commitsSincePreviousVersion: List<String>
) {
    val previousOrInitialVersion: SemVer
        get() {
            return previousVersion ?: nextVersion
        }
    val projectSemanticVersion: SemVer
        get() {
            return if (isRelease) {
                nextVersion
            } else if (isDocsUpdate) {
                previousOrInitialVersion
            } else {
                nextVersion
            }
        }
    val projectVersion: String
        get() {
            return if (isRelease) {
                nextVersion.format(false, snapshotSuffix)
            } else if (isDocsUpdate) {
                previousOrInitialVersion.format(false, snapshotSuffix)
            } else {
                nextVersion.format(true, snapshotSuffix)
            }
        }

    val documentationVersion: String
        get() {
            return if (isRelease) {
                // we're releasing a new version of the library, use the newest version
                nextVersion.format(false, snapshotSuffix)
            } else if (isDocsUpdate) {
                // we're publishing an update to the documentation site without releasing an actual update to the library
                previousOrInitialVersion.format(false, snapshotSuffix)
            } else {
                // we're just developing, use the current snapshot version
                nextVersion.format(true, snapshotSuffix)
            }
        }

    override fun toString(): String = projectVersion

    fun debug(): String {
        return """
            |ProjectVersion(
            |    projectVersion=$projectVersion
            |    documentationVersion=$documentationVersion
            |    isRelease=$isDocsUpdate
            |    snapshotSuffix=$snapshotSuffix
            |    latestSha=$latestSha
            |    commitsSincePreviousVersion=${commitsSincePreviousVersion.size}
            |)
        """.trimMargin()
    }

    fun log() {
        println(
            buildString {
                if (previousVersion == null) {
                    appendLine("$projectVersion ($latestSha)")
                } else {
                    appendLine("${previousVersion.format()} ($previousSha) -> $projectVersion ($latestSha)")
                }

                for (commit in commitsSincePreviousVersion) {
                    appendLine("  - $commit")
                }
            }
        )
    }

    companion object {
        fun get(
            project: Project,
            logChanges: Boolean = false,
            failWithUncommittedChanges: Boolean = false,
            failIfNotRelease: Boolean = false,
            snapshotSuffix: String = "-SNAPSHOT",
            initialVersion: String = "0.1.0",
            majorVersionBumpCommitPrefix: String = "[major]",
            minorVersionBumpCommitPrefix: String = "[minor]"
        ): ProjectVersion = with(project) {
            val gitShell = Git(this)

            val latestTagName = gitShell.getLatestTagName()
            val latestTagSha = gitShell.getLatestTagSha()
            val currentSha = gitShell.getCurrentSha()
            val commitsSinceLastTag = gitShell.getCommitsSinceLastTag(latestTagName)
            val isRelease = hasProperty("release")
            val isDocsUpdate = !isRelease && hasProperty("releaseDocs")
            val hasUncommittedChanges = gitShell.hasUncommittedChanges()

            val isFirstVersion = latestTagName.isBlank()

            val previousVersion: SemVer? = if (isFirstVersion) {
                null
            } else {
                SemVer.parse(latestTagName)
            }

            val nextVersion: SemVer = previousVersion
                ?.bump(
                    hasUncommittedChanges = hasUncommittedChanges,
                    commitsSinceLastTag = commitsSinceLastTag,
                    minorVersionBumpCommitPrefix = minorVersionBumpCommitPrefix,
                    majorVersionBumpCommitPrefix = majorVersionBumpCommitPrefix,
                )
                ?: SemVer.parse(initialVersion)

            // make checks on version
            if (failWithUncommittedChanges) {
                check(!hasUncommittedChanges) { "There are uncommitted changes!" }
            }
            if (failIfNotRelease) {
                check(isRelease) { "This is not a release build!" }
            }

            return ProjectVersion(
                previousVersion = previousVersion,
                nextVersion = nextVersion,
                isRelease = isRelease,
                isDocsUpdate = isDocsUpdate,
                snapshotSuffix = snapshotSuffix,
                latestSha = currentSha,
                previousSha = latestTagSha,
                commitsSincePreviousVersion = commitsSinceLastTag
            ).also {
                if (logChanges) {
                    it.log()
                }
            }
        }
    }
}

fun SemVer.format(
    isSnapshot: Boolean = false,
    snapshotSuffix: String = "",
): String {
    return if (isSnapshot) "$major.$minor.$patch$snapshotSuffix" else "$major.$minor.$patch"
}

fun SemVer.bump(
    hasUncommittedChanges: Boolean,
    commitsSinceLastTag: List<String>,
    minorVersionBumpCommitPrefix: String,
    majorVersionBumpCommitPrefix: String,
): SemVer {
    var (_major, _minor, _patch) = this

    _patch++

    for (commit in commitsSinceLastTag) {
        if (commit.startsWith(minorVersionBumpCommitPrefix)) {
            _patch = 0
            _minor++
        } else if (commit.startsWith(majorVersionBumpCommitPrefix)) {
            _patch = 0
            _minor = 0
            _major++
        }
    }

    return SemVer(major = _major, minor = _minor, patch = _patch)
}
