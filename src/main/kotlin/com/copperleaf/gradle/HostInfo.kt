package com.copperleaf.gradle

import org.gradle.api.Project
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.gradle.nativeplatform.platform.internal.DefaultOperatingSystem

@Suppress("UNUSED_PARAMETER")
data class HostInfo(
    val isMac: Boolean,
    val isLinux: Boolean,
    val isWindows: Boolean,
    val isCi: Boolean,
) {
    companion object {
        fun get(project: Project): HostInfo {
            val os: DefaultOperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()

            return HostInfo(
                isMac = os.isMacOsX,
                isLinux = os.isLinux,
                isWindows = os.isWindows,
                isCi = System.getenv("CI")?.toBoolean() == true,
            )
        }
    }
}
