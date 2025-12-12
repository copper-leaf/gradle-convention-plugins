import com.copperleaf.gradle.Sonatype

val openSonatypeStagingRepository by tasks.registering {
    group = "sonatype"
    description = "Open an explicit staging repository and cache the repository ID for publication"
    doLast {
        Sonatype(project).openSonatypeStagingRepository()
    }
}

val closeSonatypeStagingRepository by tasks.registering {
    group = "sonatype"
    description = "Close the explicitly configured staging repository"
    doLast {
        Sonatype(project).closeSonatypeStagingRepository()
    }
}

val writeProjectVersion by tasks.registering {
    description = "Write the project version to a file"
    doLast {
        Sonatype(project).writeProjectVersion()
    }
}

val findSonatypeStagingRepository by tasks.registering {
    description = "Find the staging repository ID from Sonatype and write it to a file"
    doLast {
        Sonatype(project).findSonatypeStagingRepository()
    }
}
