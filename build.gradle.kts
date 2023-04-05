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
    implementation(libs.gradlePlugin.ktlint)
    implementation(libs.gradlePlugin.kotest)
    implementation(libs.gradlePlugin.binaryCompatibilityValidator)
    implementation(libs.gradlePlugin.orchid)

    // libraries used by the custom convention plugins
    implementation(libs.okhttp)
    implementation(libs.semver)
}
