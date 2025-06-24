/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

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