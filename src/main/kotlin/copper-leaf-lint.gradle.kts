plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(false)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    disabledRules.set(
        setOf(
            "no-wildcard-imports",
            "import-ordering",
            "filename",
            "trailing-comma-on-call-site",
            "trailing-comma-on-declaration-site",
            "indent",
            "max-line-length",
        )
    )
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
