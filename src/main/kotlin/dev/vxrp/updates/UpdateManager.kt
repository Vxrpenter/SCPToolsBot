/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.updates

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.vxrpenter.updater.Updater
import io.github.vxrpenter.updater.priority.Priority.Companion.priority
import io.github.vxrpenter.updater.schema.Schema
import io.github.vxrpenter.updater.upstream.GitHubUpstream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties
import kotlin.time.Duration.Companion.minutes

object UpdateManager {
    fun checkUpdated() {
        val schema = Schema {
            prefixes = listOf("v", "v.")
            divider = "."
            classifier { value = "alpha"; divider = "-"; componentDivider = "."; priority = 1.priority }
            classifier { value = "beta"; divider = "-"; componentDivider = "."; priority = 2.priority }
            classifier { value = "rc"; divider = "-"; componentDivider = "."; priority = 3.priority }
        }

        Updater.checkUpdates(currentVersion = getVersion(), schema = schema, upstream = GitHubUpstream("Vxrpenter", "SCPToolsBot")) {
            periodic = 30.minutes
            notification {
                notify = true
                message = "A new version has arrived. Version {version} can be downloaded the link {url}"
            }
        }
    }

    private fun getVersion(): String {
        val properties = Properties()

        UpdateManager::class.java.getResourceAsStream("/dev/vxrp/resources/version.properties").use {
                versionPropertiesStream -> checkNotNull(versionPropertiesStream) { "Version properties file does not exist" }
            properties.load(InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8))
        }

        return properties.getProperty("version")
    }
}