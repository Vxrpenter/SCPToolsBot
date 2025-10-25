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

package dev.vxrp.updates

import dev.vxrp.configuration.Updates
import dev.vxrp.configuration.UpdatesConfigurationSegment
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.io.path.Path

/**
 *
 * This feature will be re-implemented at a later point in time,
 * currently not a priority
 *
 */

class UpdateHandler {
    private val logger = KotlinLogging.logger {}
    private val dir = System.getProperty("user.dir")

    fun checkUpdated(old: Updates, new: Updates) {
        if (old.version == new.version) return

        val changedMessage =
            "Your {filename} {type} is out of date, it's structure has been altered in the last update. Please look at the most current version of the {filename} here: {upstream}"
        val regenerateMessage = "You have activated 'regenerate' for {} file, it will be regenerated"

        logger.warn { "We have detected that you have installed an update for SCPToolsBot. The bot will now run an update check to see if your configurations are still up to date..." }

        val communalList = mutableListOf<UpdatesConfigurationSegment>()
        communalList.addAll(new.configurationUpdate)
        communalList.addAll(new.translationUpdates)
        communalList.addAll(new.regularsUpdate)

        var changed = false
        for (config in communalList) {
            if (!config.changed) continue
            changed = true
            logger.atWarn { message = changedMessage; payload = buildMap(capacity = 3) { put("{filename}", config.filename);put("{type}", config.type);put("{upstream}", config.upstream) }}
            if (!config.regenerate) continue
            logger.atWarn { message = regenerateMessage; payload = buildMap(capacity = 1) { put("{filename}", config.filename) }}
            val file = Path("$dir${config.location}").toFile()
            file.delete()
        }

        if (!changed) logger.info { "No configuration files were changed in this update" }
        if (new.additionalInformation.isNotBlank()) logger.warn { "Additional Information: ${new.additionalInformation}" }
    }
}