import gradle.kotlin.dsl.accessors._ad8564ff3f23344f83327959cde073bd.mkdocs

plugins {
    id("ru.vyarus.mkdocs")
}

mkdocs {
    extras = mapOf(
        "version" to project.version.toString()
    )
    publish.docPath = ""
}
