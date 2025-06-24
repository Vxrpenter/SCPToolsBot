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

package dev.vxrp.bot.status.handler

import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.ConnectionTable
import io.github.vxrpenter.secretlab.data.Server
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory

class StatusActivityHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(StatusActivityHandler::class.java)

    fun updateStatus(api: JDA, server: Server, instance: Instance) {
        logger.debug("Updating status of bot: ${api.selfUser.name} (${api.selfUser.id}) for server - ${server.port}")
        val currentMaintenance = ConnectionTable().queryFromTable(instance.serverPort.toString()).maintenance == true

        manageStatus(server, currentMaintenance, api)
        manageActivity(server, currentMaintenance, api)
    }

    private fun manageStatus(server: Server, maintenance: Boolean, api: JDA) {
        if (maintenance) {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            return
        }

        if (!server.online) {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            return
        }

        if (server.players?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.get(0).equals("0")) {
            api.presence.setStatus(OnlineStatus.IDLE)
            return
        }

        api.presence.setStatus(OnlineStatus.ONLINE)
    }

    private fun manageActivity(server: Server, maintenance: Boolean, api: JDA) {
        if  (maintenance) {
            api.presence.activity = Activity.customStatus(translation.status.activityMaintenance)
            return
        }

        if (!server.online) {
            api.presence.activity = Activity.customStatus(translation.status.activityOffline)
            return
        }

        if(server.players != null) {
            api.presence.activity = Activity.playing(server.players!!)
        }
    }
}