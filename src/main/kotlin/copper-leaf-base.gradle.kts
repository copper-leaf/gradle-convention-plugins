import com.copperleaf.gradle.ConventionConfig
import com.copperleaf.gradle.ProjectVersion

plugins {
    base
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

group = ConventionConfig.repoInfo(project).groupId
description = ConventionConfig.subprojectInfo(project).description

var projectVersion: ProjectVersion by project.extra
projectVersion = ProjectVersion.get(project = project, logChanges = project === rootProject)
version = projectVersion
