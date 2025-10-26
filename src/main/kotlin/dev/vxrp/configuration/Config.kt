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

package dev.vxrp.configuration

import dev.vxrp.configlite.ConfigLite
import dev.vxrp.loadTranslation

data class Config(
    val settings: Settings,
    val status: Status,
    val ticket: Ticket,
    val extra: ConfigExtra
) {
    companion object {
        val instance by lazy { returnConfig() }

        private fun returnConfig(): Config {
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

            return Config(settings = settings, status = Status.instance!!, ticket = Ticket.instance!!, ConfigExtra(commands = Commands.instance!!, updates = Updates.instance!!))
        }
    }
}

data class ConfigExtra(
    val commands: Commands,
    val updates: Updates
)