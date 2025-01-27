import gradle.kotlin.dsl.accessors._8bd4eb7b547f0bd368072089c68170a6.mkdocs

plugins {
    id("ru.vyarus.mkdocs")
}

mkdocs {
    extras = mapOf(
        "version" to project.version.toString()
    )
    publish.docPath = ""
}
