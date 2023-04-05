package com.copperleaf.gradle

open class License(
    val spdxIdentifier: String,
    val url: String,
) {
    open val urlAliases: Set<String> = emptySet()
    open val compatibleWith: Set<License> = emptySet()


// Permissive Licenses
// ---------------------------------------------------------------------------------------------------------------------

    object Apache : License(
        spdxIdentifier = "Apache-2.0",
        url = "https://opensource.org/licenses/Apache-2.0"
    ) {
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object MIT : License(
        spdxIdentifier = "MIT",
        url = "https://opensource.org/licenses/MIT"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/MIT",
            "http://www.opensource.org/licenses/mit-license.php",
            "http://www.opensource.org/licenses/mit-license.html",
            "https://raw.githubusercontent.com/bit3/jsass/master/LICENSE",
            "https://github.com/mockito/mockito/blob/main/LICENSE",
            "http://json.org/license.html",
            "https://jsoup.org/license",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object BSD2 : License(
        spdxIdentifier = "BSD-2-Clause",
        url = "https://opensource.org/licenses/BSD-2-Clause"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-2-Clause",
            "http://www.opensource.org/licenses/BSD-2-Clause",
            "http://www.opensource.org/licenses/bsd-license.php",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object BSD3 : License(
        spdxIdentifier = "BSD-3-Clause",
        url = "https://opensource.org/licenses/BSD-3-Clause"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-3-Clause",
            "http://www.jcraft.com/jzlib/LICENSE.txt",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object CDDL : License(
        spdxIdentifier = "CDDL-1.0",
        url = "https://opensource.org/licenses/CDDL-1.0"
    ) {
        override val urlAliases = setOf(
            "https://github.com/javaee/javax.annotation/blob/master/LICENSE",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

// Copyleft Licenses
// ---------------------------------------------------------------------------------------------------------------------

    object LGPL_V2 : License(
        spdxIdentifier = "LGPL-2.0",
        url = "https://opensource.org/licenses/LGPL-2.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/lgpl-2.1-standalone.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object LGPL_V3 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/LGPL-3.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/lgpl.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object GPL_V2 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/GPL-2.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/gpl-2.0-standalone.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            GPL_V2, GPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object GPL_V3 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/GPL-3.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/copyleft/gpl.html",
            "http://www.gnu.org/licenses/gpl.txt"
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            GPL_V2, GPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object EPL_V1 : License(
        spdxIdentifier = "EPL-1.0",
        url = "https://opensource.org/licenses/EPL-1.0"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-3-Clause",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object EPL_V2 : License(
        spdxIdentifier = "EPL-2.0",
        url = "https://opensource.org/licenses/EPL-2.0"
    ) {
        override val urlAliases = setOf(
            "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt",
            "https://www.eclipse.org/legal/epl-v20.html",
            "http://www.eclipse.org/legal/epl-v20.html",
            "https://www.eclipse.org/legal/epl-2.0/",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }
}
