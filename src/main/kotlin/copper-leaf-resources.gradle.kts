import com.copperleaf.gradle.ConventionConfig

plugins {
    id("io.github.skeptick.libres")
}

val subprojectInfo = ConventionConfig.subprojectInfo(project)
if (subprojectInfo.kotlinJvm) {
    tasks.getByPath("jvmProcessResources").dependsOn("libresGenerateResources")
    tasks.getByPath("jvmSourcesJar").dependsOn("libresGenerateResources")
}
if (subprojectInfo.kotlinJs) {
    tasks.getByPath("jsProcessResources").dependsOn("libresGenerateResources")
}

libres {

}
