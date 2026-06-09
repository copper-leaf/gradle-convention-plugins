import com.copperleaf.gradle.ConventionConfig
import com.copperleaf.gradle.PublishConfiguration
import com.copperleaf.gradle.Sonatype

val openSonatypeStagingRepository by tasks.registering {
    group = "sonatype"
    description = "Open an explicit staging repository and cache the repository ID for publication"
    doLast {
        Sonatype(project).openSonatypeStagingRepository()
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


val closeSonatypeStagingRepository by tasks.registering {
    group = "sonatype"
    description = "Close the explicitly configured staging repository"
    doLast {
        Sonatype(project).closeSonatypeStagingRepository()
    }
}


val deleteSonatypeStagingRepository by tasks.registering {
    group = "sonatype"
    description = "Delete the explicitly configured staging repository"
    doLast {
        Sonatype(project).deleteSonatypeStagingRepository()
    }
}

val cleanMavenLocalRepository by tasks.registering {
    group = "sonatype"
    description = "Delete ~/.m2/repository to ensure a clean slate before publishing to MavenLocal"
    doLast {
        Sonatype(project).cleanMavenLocalRepository()
    }
}

val publishToMavenCentralPortal by tasks.registering {
    group = "sonatype"
    description = "Zip ~/.m2/repository and upload it to the Sonatype Central Publishing Portal API"
    doLast {
        Sonatype(project).publishToMavenCentralPortal()
    }
}
