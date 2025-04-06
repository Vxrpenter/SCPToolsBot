package dev.vxrp.configuration

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.configuration.managers.ConfigManager
import dev.vxrp.configuration.managers.TranslationManager
import dev.vxrp.database.DatabaseManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

class ConfigurationManager {
    private val configManager = ConfigManager()
    private val translationManager = TranslationManager()

    fun initializeConfigs(): Config  {
        createConfigurations(configManager, System.getProperty("user.dir"))
        return configManager.query(
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/extra/launch-configuration.json"),
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/config.yml"),
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/status-settings.yml"),
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/ticket-settings.yml")
        )
    }

    fun initializeTranslations(config: Config): Translation {
        createTranslations(translationManager)

        return translationManager.query(System.getProperty("user.dir"), config.settings.loadTranslation)
    }

    fun initializeDatabase(config: Config) {
        val databaseManager = DatabaseManager(config, "/SCPToolsBot/database", "data.db")

        configManager.databaseManagement(
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/extra/launch-configuration.json"),
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/config.yml"),
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/status-settings.yml"),
            Path("${System.getProperty("user.dir")}/SCPToolsBot/configs/ticket-settings.yml")
        )
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

    private fun createConfigurations(configManager: ConfigManager, dir: String) {
        val configs = ArrayList<Path>()
        configs.add((Path("/SCPToolsBot/configs/extra/launch-configuration.json")))
        configs.add(Path("/SCPToolsBot/configs/config.yml"))
        configs.add(Path("/SCPToolsBot/configs/extra/color-config.json"))
        configs.add(Path("/SCPToolsBot/configs/ticket-settings.yml"))
        configs.add(Path("/SCPToolsBot/configs/status-settings.yml"))

        configManager.create(dir, configs)
    }

    private fun createTranslations(translationManager: TranslationManager) {
        val translations = ArrayList<Path>()
        translations.add(Path("/SCPToolsBot/lang/en_US.yml"))
        translations.add(Path("/SCPToolsBot/lang/de_DE.yml"))

        translationManager.create(System.getProperty("user.dir"), translations)
    }
}