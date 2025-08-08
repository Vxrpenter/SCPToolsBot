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

package dev.vxrp

import dev.vxrp.bot.Bot
import dev.vxrp.configuration.ConfigurationManager
import dev.vxrp.configuration.data.Config
import dev.vxrp.updates.UpdateManager
import io.github.freya022.botcommands.api.core.BotCommands
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

fun main() {
    logger.info("Starting up...")
    val updateManager = UpdateManager()
    updateManager.checkUpdated()

    val config = Config.instance
    val translation = ConfigurationManager.initializeTranslations(config)
    ConfigurationManager.initializeDatabase(config)
    ConfigurationManager.setLoggingLevel(config)

    updateManager.spinUpChecker(config)

    BotCommands.create {
        addSearchPath("dev.vxrp.bot")
    }
}