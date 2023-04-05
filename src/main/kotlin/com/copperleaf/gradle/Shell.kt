package com.copperleaf.gradle

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

abstract class Shell(private val project: Project) {

    protected fun runCommand(command: String): String = with(project) {
        return runCatching {
            val stdout = ByteArrayOutputStream()

            exec {
                commandLine(*command.split(' ').toTypedArray())
                standardOutput = stdout
            }

            stdout.toString().trim()
        }.getOrElse { "" }
    }
}
