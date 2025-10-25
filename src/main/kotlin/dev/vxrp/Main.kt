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

import dev.reformator.stacktracedecoroutinator.jvm.DecoroutinatorJvmApi
import dev.vxrp.configlite.ConfigLite
import dev.vxrp.configuration.Commands
import dev.vxrp.configuration.Config
import dev.vxrp.configuration.ConfigExtra
import dev.vxrp.configuration.Settings
import dev.vxrp.configuration.Status
import dev.vxrp.configuration.Ticket
import dev.vxrp.configuration.Translation
import dev.vxrp.configuration.Updates
import dev.vxrp.updates.UpdateManager
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory

private val logger = KotlinLogging.logger {}

var config: Config? = null
var translation: Translation? = null
var loadTranslation = "en_US.yml"

fun main() {
    decoroutinator()
    registerConfigurations()
    UpdateManager.checkUpdated()
}

private fun registerConfigurations() {
    val workingDirectory = System.getProperty("user.dir")

    // Register the main configuration file
    ConfigLite.register(workingDirectory, "/SCPToolsBot/configs", "config.yml")
    val settings = Settings.instance
    settings ?: throw NullPointerException("Could not load config.yml, returned null")
    loadTranslation = "${settings.loadTranslation}.yml"

    // Register translation
    ConfigLite.register(workingDirectory, "/SCPToolsBot/lang", "en_US.yml")
    ConfigLite.register(workingDirectory, "/SCPToolsBot/lang", "de_DE.yml")

    // Register remaining configs
    ConfigLite.register(workingDirectory, "/SCPToolsBot/configs", "status.yml")
    ConfigLite.register(workingDirectory, "/SCPToolsBot/configs", "tickets.yml")
    ConfigLite.register(workingDirectory, "/SCPToolsBot/configs/extra", "commands.json")
    ConfigLite.register(workingDirectory, "/SCPToolsBot/configs/extra", "updates.json")

    config = Config(settings = settings, status = Status.instance!!, ticket = Ticket.instance!!, ConfigExtra(commands = Commands.instance!!, updates = Updates.instance!!))
    translation = Translation.instance
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