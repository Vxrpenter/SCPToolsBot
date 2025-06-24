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
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.updatesScope
import dev.vxrp.util.upstreamVersion
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.hours

class UpdateManager() {
    private val logger = LoggerFactory.getLogger(UpdateManager::class.java)

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
        Timer().runWithTimer(1.hours, updatesScope) {
            try {
                upstreamVersion = UpdateHandler().checkForUpdatesByTag(config, "https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")
            } catch (_: Exception) {
                logger.error("Could not proceed with update check correctly")
            }
        }
    }
}