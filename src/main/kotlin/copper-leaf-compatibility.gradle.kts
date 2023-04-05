plugins {
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

apiValidation {
    if (findProject(":docs") != null) {
        ignoredProjects.add("docs")
    }
}
