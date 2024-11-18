package dev.vxrp

import dev.vxrp.bot.BotManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.configuration.managers.ConfigManager
import dev.vxrp.configuration.managers.TranslationManager
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val logger = LoggerFactory.getLogger("dev.vxrp.Main")
    logger.info("Starting up...")

    val configManager = ConfigManager()
    val translationManager = TranslationManager()

    initializeConfiguration(configManager, System.getProperty("user.dir"))
    initializeTranslations(translationManager)
    val config = configManager.query(System.getProperty("user.dir"), "/configs/config.yml")
    val translation = translationManager.query(System.getProperty("user.dir"), config.loadTranslation)

    ScpToolsBot(config, translation)
}

fun initializeConfiguration(configManager: ConfigManager, dir : String) {
    val configs = ArrayList<Path>()
    configs.add(Path("/configs/config.yml"))
    configs.add(Path("/configs/color-config.yml"))

    configManager.create(dir, configs)
}

fun initializeTranslations(translationManager: TranslationManager) {
    val translations = ArrayList<Path>()
    translations.add(Path("/lang/en_us.yml"))
    translations.add(Path("/lang/de_de.yml"))

    translationManager.create(System.getProperty("user.dir"), translations)
}

class ScpToolsBot(val config: Config, val translation: Translation) {
    init {
        BotManager(config)
    }
}