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
    additionalEditorconfig.set(
        mapOf(
            "insert_final_newline" to "true",
            "max_line_length" to "120",
            "ktlint_function_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to "unset",

            "ktlint_standard_no-wildcard-imports" to "disabled",
            "ktlint_standard_import-ordering" to "disabled",
            "ktlint_standard_filename" to "disabled",
            "ktlint_standard_trailing-comma-on-call-site" to "disabled",
            "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
            "ktlint_standard_string-template-indent" to "disabled",
            "ktlint_standard_max-line-length" to "disabled",
            "ktlint_standard_property-naming" to "disabled",
            "ktlint_standard_multiline-expression-wrapping" to "disabled",
            "ktlint_standard_function-signature" to "disabled",
            "ktlint_standard_no-blank-line-in-list" to "disabled",
            "ktlint_standard_blank-line-before-declaration" to "disabled",
            "ktlint_standard_indent" to "disabled",
            "ktlint_standard_no-empty-first-line-in-class-body" to "disabled",
            "ktlint_standard_comment-wrapping" to "disabled",
            "ktlint_standard_parameter-list-wrapping" to "disabled",
            "ktlint_standard_argument-list-wrapping" to "disabled",
            "ktlint_standard_wrapping" to "disabled",
            "ktlint_standard_function-naming" to "disabled",
            "ktlint_standard_no-consecutive-comments" to "disabled",
            "ktlint_standard_chain-method-continuation" to "disabled",
            "ktlint_standard_function-expression-body" to "disabled",
            "ktlint_standard_function-literal" to "disabled",
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
