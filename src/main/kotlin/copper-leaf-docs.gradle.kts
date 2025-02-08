plugins {
    id("ru.vyarus.mkdocs")
}

mkdocs {
    extras = mapOf(
        "version" to project.version.toString()
    )
    publish.docPath = ""
}
