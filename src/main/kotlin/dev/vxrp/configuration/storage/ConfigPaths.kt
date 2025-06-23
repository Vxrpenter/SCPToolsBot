package dev.vxrp.configuration.storage

import kotlin.io.path.Path

/**
 * These are all major configuration paths for important files.
 * They are updated on every start using UpdatesFileHandler().setConfigPaths().
 * If this update does not happen, they will fall back to these backup paths.
 */
class ConfigPaths {
    var configPath = Path("/SCPToolsBot/configs/config.yml")
    var ticketPath = Path("/SCPToolsBot/configs/ticket-settings.yml")
    var statusPath = Path("/SCPToolsBot/configs/status-settings.yml")
    var commandsPath = Path("/SCPToolsBot/configs/extra/commands.json")
    var launchConfigurationPath = Path("/SCPToolsBot/configs/extra/launch-configuration.json")

    var enUsPath = Path("/SCPToolsBot/lang/en_US.yml")
    var deDePath = Path("/SCPToolsBot/lang/de_DE.yml")
}