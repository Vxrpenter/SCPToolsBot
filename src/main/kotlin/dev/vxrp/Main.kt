package dev.vxrp

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import dev.vxrp.api.github.Github
import dev.vxrp.bot.BotManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.configuration.managers.ConfigManager
import dev.vxrp.configuration.managers.TranslationManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    Github().checkForUpdatesByTag("https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")

    val logger = LoggerFactory.getLogger("dev.vxrp.Main")
    logger.info("Starting up...")

    val configManager = ConfigManager()
    val translationManager = TranslationManager()

    initializeConfiguration(configManager, System.getProperty("user.dir"))
    initializeTranslations(translationManager)
    val config = configManager.query(System.getProperty("user.dir"), "/configs/config.yml")
    setLoggingLevel(config)
    val translation = translationManager.query(System.getProperty("user.dir"), config.loadTranslation)

    ScpToolsBot(config, translation)
}

fun initializeConfiguration(configManager: ConfigManager, dir : String) {
    val configs = ArrayList<Path>()
    configs.add(Path("/configs/config.yml"))
    configs.add(Path("/configs/color-config.json"))

    configManager.create(dir, configs)
}

fun setLoggingLevel(config: Config) {
    var level = Level.INFO
    if (config.debug) {
        level = Level.DEBUG
    }
    if (config.advancedDebug) {
        level = Level.TRACE
    }

    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val log = loggerContext.exists(Logger.ROOT_LOGGER_NAME)
    log.level = level
}

fun initializeTranslations(translationManager: TranslationManager) {
    val translations = ArrayList<Path>()
    translations.add(Path("/lang/en_us.yml"))
    translations.add(Path("/lang/de_de.yml"))

    translationManager.create(System.getProperty("user.dir"), translations)
}

class ScpToolsBot(currentConfig: Config, currentTranslation: Translation) {
    var config = currentConfig
    var translation = currentTranslation

    init {
        BotManager(config, translation)
    }

}