package dev.vxrp

import dev.vxrp.configuration.ConfigurationManager
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.updates.Updates
import dev.vxrp.util.launch.LaunchOptionManager
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

fun main() {
    logger.info("Starting up...")
    val updates = Updates()

    val configurationManager = ConfigurationManager()

    val config = configurationManager.initializeConfigs()

    val translation = configurationManager.initializeTranslations(config)
    configurationManager.initializeDatabase(config)
    configurationManager.setLoggingLevel(config)

    updates.checkForUpdatesByTag(config, "https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")
    ScpToolsBot(config, translation)
}

class ScpToolsBot(config: Config, translation: Translation) {
    init {
        LaunchOptionManager(config, translation).startupBots()
    }
}