package dev.vxrp

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import dev.vxrp.api.github.Github
import dev.vxrp.bot.BotManager
import dev.vxrp.configuration.ConfigurationManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.configuration.managers.ConfigManager
import dev.vxrp.configuration.managers.TranslationManager
import dev.vxrp.database.DatabaseManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

val logger = LoggerFactory.getLogger("dev.vxrp.Main")

fun main() {
    Github().checkForUpdatesByTag("https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")
    logger.info("Starting up...")

    val configurationManager = ConfigurationManager()

    val config = configurationManager.initializeConfigs()
    val translation = configurationManager.initializeTranslations(config)
    configurationManager.initializeDatabase(config)
    configurationManager.setLoggingLevel(config)

    ScpToolsBot(config, translation)
}

class ScpToolsBot(currentConfig: Config, currentTranslation: Translation) {
    var config = currentConfig
    var translation = currentTranslation

    init {
        BotManager(config, translation).init()
    }
}