package com.copperleaf.gradle

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

abstract class Shell(private val project: Project) {

    protected fun runCommand(command: String): String = with(project) {
        return providers.exec {
            commandLine(command.split(' '))
        }.standardOutput.asText.get().trim()
    }
}
