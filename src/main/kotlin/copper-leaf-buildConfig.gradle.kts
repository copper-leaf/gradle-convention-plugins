plugins {
    id("com.github.gmazzo.buildconfig")
}

buildConfig {
    useKotlinOutput {
        internalVisibility = true
        topLevelConstants = true
    }
}
