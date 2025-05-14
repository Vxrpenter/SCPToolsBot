package dev.vxrp

import dev.vxrp.api.updates.Updates
import dev.vxrp.configuration.ConfigurationManager
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

fun main() {
    logger.info("Starting up...")

    val configurationManager = ConfigurationManager()

    val config = configurationManager.initializeConfigs()

    val translation = configurationManager.initializeTranslations(config)
    configurationManager.initializeDatabase(config)
    configurationManager.setLoggingLevel(config)

    Updates(config).checkForUpdatesByTag("https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")
    ScpToolsBot(config, translation)
}

class ScpToolsBot(config: Config, translation: Translation) {
    init {
        LaunchOptionManager(config, translation).startupBots()
    }
}