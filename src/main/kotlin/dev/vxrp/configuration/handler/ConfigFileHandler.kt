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

package dev.vxrp.configuration.handler

import com.charleskorn.kaml.Yaml
import dev.vxrp.bot.commands.data.CommandList
import dev.vxrp.bot.status.data.Status
import dev.vxrp.bot.ticket.data.Ticket
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.ConfigExtra
import dev.vxrp.configuration.data.Settings
import dev.vxrp.database.tables.database.ApplicationTypeTable
import dev.vxrp.util.launch.data.LaunchConfiguration
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class ConfigFileHandler {
    private val logger = LoggerFactory.getLogger(ConfigFileHandler::class.java)

    fun create(dir: String, files: List<Path>) {
        Files.createDirectories(Path("$dir/SCPToolsBot/configs/extra/"))

        for (file in files) {
            val content = ConfigFileHandler::class.java.getResourceAsStream(file.toString())

            val path = Path("$dir$file")

            val currentFile = path.toFile()
            if (!currentFile.exists()) {
                currentFile.createNewFile()
                logger.info("Created configuration file $dir$file")

                if (content != null) {
                    currentFile.appendBytes(content.readBytes())
                    logger.info("Wrote contents to $dir$file")
                }
            }
        }
    }

    fun query(dir: String, configPath: Path, statusPath: Path, ticketPath: Path, commandsPath: Path, launchConfigurationPath: Path): Config {
        val settings = Yaml.default.decodeFromString(Settings.serializer(), Path("$dir$configPath").toFile().readText())
        val status = Yaml.default.decodeFromString(Status.serializer(), Path("$dir$statusPath").toFile().readText())
        val ticket = Yaml.default.decodeFromString(Ticket.serializer(), Path("$dir$ticketPath").toFile().readText())

        val commands = Json.decodeFromString(CommandList.serializer(), Path("$dir$commandsPath").toFile().readText())
        val launchConfiguration = Json.decodeFromString(LaunchConfiguration.serializer(), Path("$dir$launchConfigurationPath").toFile().readText())

        return Config(settings = settings, status = status, ticket = ticket, ConfigExtra(commands = commands, launchConfiguration = launchConfiguration))
    }

    fun databaseManagement(config: Config) {
        val idList = mutableListOf<String>()

        for (type in config.ticket.applicationTypes) {
            if (ApplicationTypeTable().exists(type.roleID)) {
                ApplicationTypeTable().addToDatabase(type.roleID, false, null,null)
            }

            idList.add(type.roleID)
        }

        ApplicationTypeTable().deleteRedundantValues(idList)
    }
}