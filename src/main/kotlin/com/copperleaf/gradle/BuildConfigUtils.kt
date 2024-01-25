package com.copperleaf.gradle

import com.github.gmazzo.buildconfig.BuildConfigExtension
import org.gradle.api.Project

fun BuildConfigExtension.projectVersion(project: Project, propertyName: String = "VERSION") {
    stringField(propertyName, project.version.toString())
}

fun BuildConfigExtension.stringField(key: String, value: String) {
    buildConfigField("String", key, "\"$value\"")
}

fun BuildConfigExtension.booleanField(key: String, value: Boolean) {
    buildConfigField("boolean", key, value.toString())
}
