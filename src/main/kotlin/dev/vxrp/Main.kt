package dev.vxrp

import dev.vxrp.api.github.Github
import dev.vxrp.configuration.ConfigurationManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

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
        LaunchOptionManager(config, translation).startupBots()
    }
}