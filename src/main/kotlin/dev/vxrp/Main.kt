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

import dev.minn.jda.ktx.interactions.commands.slash
import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.reformator.stacktracedecoroutinator.jvm.DecoroutinatorJvmApi
import dev.vxrp.bot.main.Bot
import dev.vxrp.bot.status.StatusBot
import dev.vxrp.configuration.Config
import dev.vxrp.configuration.Translation
import dev.vxrp.updates.UpdateManager
import io.github.oshai.kotlinlogging.KotlinLogging
import java.lang.management.ManagementFactory
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
var loadTranslation = "en_US.yml"

fun main() {
    decoroutinator()

    // Configuration and Translation
    logger.info { "Registering configuration files" }
    val config = Config.instance

    logger.info { "Registering translation files" }
    val translation = Translation.instance!!

    // Check updates
    UpdateManager(config).checkUpdated()

    // Starting up the main bot
    val api = Bot(config, translation).api
    api ?: exitProcess(1)
    api.awaitReady()

    // Starting up the status bots
    if (config.status.active) {
        val apiList = StatusBot(config, translation).apiList!!


    }
}

private fun decoroutinator() {
    val logger = KotlinLogging.logger {}
    val args = ManagementFactory.getRuntimeMXBean().inputArguments

    if ("-XX:+EnableDynamicAgentLoading" !in args) logger.warn { "This project uses a serviceability tool, please use '-XX:+EnableDynamicAgentLoading' on startup" }

    if ("-XX:+AllowEnhancedClassRedefinition" in args) {
        logger.info { "Skipping stacktrace-decoroutinator as enhanced hotswap is active" }
    } else if ("--no-decoroutinator" in args) {
        logger.info { "Skipping stacktrace-decoroutinator as --no-decoroutinator is specified" }
    } else {
        DecoroutinatorJvmApi.install()
    }
}