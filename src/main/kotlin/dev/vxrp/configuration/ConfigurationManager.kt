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

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import dev.vxrp.bot.commands.data.CommandList
import dev.vxrp.bot.status.data.Status
import dev.vxrp.bot.ticket.data.Ticket
import dev.vxrp.configlite.ConfigLite
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.ConfigExtra
import dev.vxrp.configuration.data.Settings
import dev.vxrp.configuration.data.Translation
import dev.vxrp.configuration.handler.ConfigFileHandler
import dev.vxrp.configuration.handler.TranslationFileHandler
import dev.vxrp.configuration.storage.ConfigPaths
import dev.vxrp.database.DatabaseManager
import dev.vxrp.util.launch.data.LaunchConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

class ConfigurationManager {
    private val configFileHandler = ConfigFileHandler()
    private val translationFileHandler = TranslationFileHandler()

    private val workDir = System.getProperty("user.dir")

    fun initializeConfigs(): Config  {
        ConfigLite.register(workDir, ConfigPaths().configPath.parent.toString(), ConfigPaths().configPath.fileName.toString())
        ConfigLite.register(workDir, ConfigPaths().ticketPath.parent.toString(), ConfigPaths().ticketPath.fileName.toString())
        ConfigLite.register(workDir, ConfigPaths().statusPath.parent.toString(), ConfigPaths().statusPath.fileName.toString())
        ConfigLite.register(workDir, ConfigPaths().commandsPath.parent.toString(), ConfigPaths().commandsPath.fileName.toString())
        ConfigLite.register(workDir, ConfigPaths().launchConfigurationPath.parent.toString(), ConfigPaths().launchConfigurationPath.fileName.toString())

        return Config(Settings.instance!!, Status.instance!!, Ticket.instance!!, ConfigExtra(CommandList.instance!!, LaunchConfiguration.instance!!))
    }

    fun initializeTranslations(config: Config): Translation {
        createTranslations(translationFileHandler)

        return translationFileHandler.query(System.getProperty("user.dir"), config.settings.loadTranslation)
    }

    fun initializeDatabase(config: Config) {
        DatabaseManager(config, Path("/SCPToolsBot/database/data.db"))

        configFileHandler.databaseManagement(config)
    }

    fun setLoggingLevel(config: Config) {
        var level = Level.INFO
        if (config.settings.debug) {
            level = Level.DEBUG
        }
        if (config.settings.advancedDebug) {
            level = Level.TRACE
        }

        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val log = loggerContext.exists(Logger.ROOT_LOGGER_NAME)
        log.level = level
    }

    private fun createTranslations(translationFileHandler: TranslationFileHandler) {
        val translations = ArrayList<Path>()
        translations.add(ConfigPaths().enUsPath)
        translations.add(ConfigPaths().deDePath)

        translationFileHandler.create(workDir, translations)
    }
}