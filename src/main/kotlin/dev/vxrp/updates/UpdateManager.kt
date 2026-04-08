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

import dev.vxrp.configuration.data.Config
import dev.vxrp.updates.handler.UpdateHandler
import dev.vxrp.updates.handler.UpdatesFileHandler
import io.github.vxrpenter.updater.Updater
import io.github.vxrpenter.updater.priority.Priority.Companion.priority
import io.github.vxrpenter.updater.schema.builder.Schema
import io.github.vxrpenter.updater.upstream.GitHubUpstream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.time.Duration.Companion.hours

class UpdateManager {
    fun checkUpdated() {
        val dir = System.getProperty("user.dir")
        val fileHandler = UpdatesFileHandler()
        fileHandler.create(dir)

        try { fileHandler.queryOld(dir) } catch (_: Exception) {
            fileHandler.delete(dir)
            fileHandler.create(dir)
        }


        fileHandler.setConfigPaths(fileHandler.queryNew())
        UpdateHandler().checkUpdated(fileHandler.queryOld(dir), fileHandler.queryNew())
    }

    fun spinUpChecker(config: Config) {
        val schema = Schema {
            prefixes = listOf("v", "v."); divider = "."
            classifier { value = "alpha"; divider = "-"; componentDivider = "."; priority = 1.priority; ignore = config.settings.updates.ignoreBeta }
            classifier { value = "beta"; divider = "-"; componentDivider = "."; priority = 2.priority; ignore = config.settings.updates.ignoreAlpha  }
            //classifier { value = "rc"; divider = "-"; componentDivider = "."; priority = 3.priority; ignore = config.settings.updates.ignoreRc  }
        }

        val properties = Properties()
        UpdateManager::class.java.getResourceAsStream("/dev/vxrp/version.properties").use {
                versionPropertiesStream -> checkNotNull(versionPropertiesStream) { "Version properties file does not exist" }
            properties.load(InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8))
        }
        val version = properties.getProperty("version")

        val upstream = GitHubUpstream(user = "Vxrpenter", repo = "SCPToolsBot")
        Updater.checkUpdates(currentVersion = version, schema = schema, upstream = upstream) {
            periodic = 1.hours
            notification {
                notify = true
                message = "A new version has been found, you can download Version {version} here {url}"
            }
        }
    }
}