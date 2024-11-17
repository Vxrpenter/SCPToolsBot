package dev.vxrp

import dev.vxrp.configuration.managers.ConfigManager
import dev.vxrp.configuration.managers.TranslationManager
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val logger = LoggerFactory.getLogger("dev.vxrp.Main")
    logger.info("Starting up...")

    initializeConfiguration()
}

fun initializeConfiguration() {
    val dir = System.getProperty("user.dir")

    val configs = ArrayList<Path>()
    configs.add(Path("/configs/config.yml"))
    configs.add(Path("/configs/color-config.yml"))

    val configManager = ConfigManager()
    configManager.create(dir, configs)

    val translations = ArrayList<Path>()
    translations.add(Path("/translations/en_us.yml"))
    translations.add(Path("/translations/de_de.yml"))

    val translationManager = TranslationManager()
    translationManager.create(dir, translations)
}