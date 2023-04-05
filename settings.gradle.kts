rootProject.name = "gradle-convention-plugins"

dependencyResolutionManagement {
    versionCatalogs {
        create("conventionLibs") {
            from(files("./gradle/conventionLibs.versions.toml"))
        }
    }
}
