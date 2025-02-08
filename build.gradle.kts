plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    // third-party gradle plugins
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.kotlinSerialization)
    implementation(libs.gradlePlugin.kotlinCompose)
    implementation(libs.gradlePlugin.kotlinComposeCompiler)
    implementation(libs.gradlePlugin.ktlint)
    implementation(libs.gradlePlugin.binaryCompatibilityValidator)
    implementation(libs.gradlePlugin.buildConfig)
    implementation(libs.gradlePlugin.mokoResources)
    implementation(libs.gradlePlugin.sqldelight)
    implementation(libs.gradlePlugin.mkdocs)

    implementation(libs.gradlePlugin.jetbrainsIntelliJ)
    implementation(libs.gradlePlugin.jetbrainsChangelog)

    // libraries used by the custom convention plugins
    implementation(libs.okhttp)
    implementation(libs.semver)
}
