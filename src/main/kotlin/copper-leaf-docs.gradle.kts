import com.copperleaf.gradle.ConventionConfig

plugins {
    id("com.eden.orchidPlugin")
}

@Suppress("JcenterRepositoryObsolete")
repositories {
    @Suppress("DEPRECATION")
    jcenter()
}

dependencies {
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidDocs:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidCopper:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidGithub:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidKotlindoc:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidPluginDocs:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidSnippets:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidWritersBlocks:0.21.1")
    orchidRuntimeOnly("com.google.inject:guice:5.1.0")
}

// Orchid setup
// ---------------------------------------------------------------------------------------------------------------------

orchid {
    githubToken = ConventionConfig.publishConfig(project).githubToken
    version = ConventionConfig.projectVersion(project).documentationVersion
}

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidDeploy by tasks

orchidBuild.mustRunAfter(check)
build.dependsOn(orchidBuild)

val publish by tasks.registering {
    dependsOn(orchidDeploy)
}
