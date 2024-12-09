plugins {
    id("io.github.fstaudt.hugo")
}

hugo {
    version.set("0.139.3")
    sourceDirectory.set("src/main/hugo")
}

tasks.hugoBuild {
    outputDirectory.set(project.layout.buildDirectory.asFile.get().resolve("dist/hugo"))
}
