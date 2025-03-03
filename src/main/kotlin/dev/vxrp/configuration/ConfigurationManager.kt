package dev.vxrp.configuration

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import dev.vxrp.ScpToolsBot
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
    private val logger = LoggerFactory.getLogger(ConfigurationManager::class.java)

    private val configManager = ConfigManager()
    private val translationManager = TranslationManager()

    fun initializeConfigs(): Config  {
        createConfigurations(configManager, System.getProperty("user.dir"))
        return  configManager.query(
            Path("${System.getProperty("user.dir")}/configs/launch-configuration.json"),
            Path("${System.getProperty("user.dir")}/configs/config.yml"),
            Path("${System.getProperty("user.dir")}/configs/status-settings.json"),
            Path("${System.getProperty("user.dir")}/configs/ticket-settings.json")
        )
    }

    fun initializeTranslations(config: Config): Translation {
        createTranslations(translationManager)

        return translationManager.query(System.getProperty("user.dir"), config.settings.loadTranslation)
    }

    fun initializeDatabase(config: Config) {
        val databaseManager = DatabaseManager(config, "database", "data.db")

        configManager.databaseManagement(
            Path("${System.getProperty("user.dir")}/configs/launch-configuration.json"),
            Path("${System.getProperty("user.dir")}/configs/config.yml"),
            Path("${System.getProperty("user.dir")}/configs/status-settings.json"),
            Path("${System.getProperty("user.dir")}/configs/ticket-settings.json")
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
        configs.add(Path("/configs/config.yml"))
        configs.add(Path("/configs/color-config.json"))
        configs.add(Path("/configs/ticket-settings.json"))
        configs.add(Path("/configs/status-settings.json"))

        configManager.create(dir, configs)
    }

    private fun createTranslations(translationManager: TranslationManager) {
        val translations = ArrayList<Path>()
        translations.add(Path("/lang/en_US.yml"))
        translations.add(Path("/lang/de_DE.yml"))

        translationManager.create(System.getProperty("user.dir"), translations)
    }
}