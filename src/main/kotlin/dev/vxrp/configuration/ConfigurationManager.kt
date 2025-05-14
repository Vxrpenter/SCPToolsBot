package dev.vxrp.configuration

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import dev.vxrp.configuration.handler.ConfigFileHandler
import dev.vxrp.configuration.handler.TranslationFileHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.DatabaseManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

class ConfigurationManager {
    private val configFileHandler = ConfigFileHandler()
    private val translationFileHandler = TranslationFileHandler()

    private val workDir = System.getProperty("user.dir")

    private val configPath = Path("/SCPToolsBot/configs/config.yml")
    private val ticketPath = Path("/SCPToolsBot/configs/ticket-settings.yml")
    private val statusPath = Path("/SCPToolsBot/configs/status-settings.yml")
    private val commandsPath = Path("/SCPToolsBot/configs/extra/commands.json")
    private val launchConfigurationPath = Path("/SCPToolsBot/configs/extra/launch-configuration.json")

    private val enUsPath = Path("/SCPToolsBot/lang/en_US.yml")
    private val deDePath = Path("/SCPToolsBot/lang/de_DE.yml")

    fun initializeConfigs(): Config  {
        createConfigurations(configFileHandler, workDir)
        return configFileHandler.query(dir = workDir, configPath = configPath, statusPath = statusPath, ticketPath = ticketPath, commandsPath = commandsPath, launchConfigurationPath= launchConfigurationPath)
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

    private fun createConfigurations(configFileHandler: ConfigFileHandler, dir: String) {
        val configs = ArrayList<Path>()
        configs.add(configPath)
        configs.add(ticketPath)
        configs.add(statusPath)
        configs.add(commandsPath)
        configs.add(launchConfigurationPath)

        configFileHandler.create(dir, configs)
    }

    private fun createTranslations(translationFileHandler: TranslationFileHandler) {
        val translations = ArrayList<Path>()
        translations.add(enUsPath)
        translations.add(deDePath)

        translationFileHandler.create(workDir, translations)
    }
}