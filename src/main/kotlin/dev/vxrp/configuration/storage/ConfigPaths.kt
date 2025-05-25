package dev.vxrp.configuration.storage

import kotlin.io.path.Path

class ConfigPaths {
    val configPath = Path("/SCPToolsBot/configs/config.yml")
    val ticketPath = Path("/SCPToolsBot/configs/ticket-settings.yml")
    val statusPath = Path("/SCPToolsBot/configs/status-settings.yml")
    val commandsPath = Path("/SCPToolsBot/configs/extra/commands.json")
    val launchConfigurationPath = Path("/SCPToolsBot/configs/extra/launch-configuration.json")

    val enUsPath = Path("/SCPToolsBot/lang/en_US.yml")
    val deDePath = Path("/SCPToolsBot/lang/de_DE.yml")
}