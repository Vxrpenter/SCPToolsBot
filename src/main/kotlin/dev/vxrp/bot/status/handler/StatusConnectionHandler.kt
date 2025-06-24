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
import dev.vxrp.util.statusApiSessionStatus
import dev.vxrp.util.statusServerSessionStatus
import io.github.vxrpenter.secretlab.data.Server
import io.github.vxrpenter.secretlab.data.ServerInfo
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory

private val reconnectAttempt = hashMapOf<Int, Int>()
private var retryFetchData = 0

class StatusConnectionHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(StatusConnectionHandler::class.java)

    fun postApiConnectionUpdate(api: JDA, info: ServerInfo?) {
        val apiStatus = ConnectionTable().queryFromTable("api").status == true

        if (info != null && info.success) ConnectionTable().databaseNotExists("api", true)
        else ConnectionTable().databaseNotExists("api", false)

        if (info != null && info.success) {
            logger.debug("Connection to api successful")
            if (apiStatus && statusApiSessionStatus) return
            if (config.status.postServerStatus && !apiStatus) info.let { StatusMessageHandler(config, translation).postConnectionEstablished(api, it) }
            ConnectionTable().postConnectionToDatabase("api", true)
            retryFetchData = 0
            statusApiSessionStatus = true
            logger.info("Regained connection to secretlab api")
        } else {
            logger.debug("Connection to api failed")
            if (!apiStatus) return
            if (retryFetchData == config.status.retryToFetchData+1) return
            if (retryFetchData == config.status.retryToFetchData) {
                var errorMessage = "No json body was returned for serialization"
                if (info != null && info.error != null) errorMessage = info.error!!
                logger.error("SecretLabApi connection lost, request returned unsuccessful, $errorMessage")
                if (config.status.postServerStatus) StatusMessageHandler(config, translation).postConnectionLost(api, config.status.retryToFetchData)
                retryFetchData += 1
                ConnectionTable().postConnectionToDatabase("api", false)
                return
            }
            if (retryFetchData >= config.status.suspectRateLimitUntil && retryFetchData != config.status.retryToFetchData+1) {
                logger.warn("Failed $retryFetchData consecutive times to connect to the api. Suspecting an api outage or invalid key. Retrying ${config.status.retryToFetchData- retryFetchData} more times")
            }
            if (retryFetchData < config.status.suspectRateLimitUntil) {
                logger.warn("Failed to access the secretlab api, suspecting rate limiting or small outage, retrying in ${config.status.checkRate} seconds")
            }
            retryFetchData += 1
            statusApiSessionStatus = false
        }
    }

    fun postStatusUpdate(server: Server, api: JDA, instance: Instance, info: ServerInfo?) {
        ConnectionTable().databaseNotExists(server.port.toString(), server.online)
        reconnectAttempt.putIfAbsent(server.port, 0)

        val serverStatus = ConnectionTable().queryFromTable(server.port.toString()).status == true
        statusServerSessionStatus.putIfAbsent(server.port, serverStatus)

        if (server.online) {
            logger.debug("Connection to server ${instance.name} (${instance.serverPort}), established and returned online")
            if (serverStatus && statusServerSessionStatus[server.port] == true) return
            if (config.status.postServerStatus && !serverStatus) StatusMessageHandler(config, translation).postConnectionOnline(api, instance, info!!)
            reconnectAttempt[server.port] = 0
            statusServerSessionStatus[server.port] = true
            ConnectionTable().postConnectionToDatabase(server.port.toString(), true)
            logger.info("Connection to server ${instance.name} (${instance.serverPort}) regained")
        } else {
            logger.debug("Connection to server ${instance.name} (${instance.serverPort}), not established and returned offline")
            if (!serverStatus) return

            if (reconnectAttempt[server.port]!! == instance.retries + 1) return
            if (reconnectAttempt[server.port]!! == instance.retries) {
                logger.warn("Completely lost connection to server - ${instance.name}. Server is probably offline/unreachable")

                if (config.status.postServerStatus) StatusMessageHandler(config, translation).postConnectionOffline(api, instance, info!!)
                reconnectAttempt[server.port] = instance.retries + 1
                ConnectionTable().postConnectionToDatabase(server.port.toString(), false)
                return
            }
            logger.warn("Failed to query data from \"${instance.name}\", trying to reconnect ${ instance.retries - reconnectAttempt[server.port]!!} more times")
            reconnectAttempt[server.port] = reconnectAttempt[server.port]!! + 1
            statusServerSessionStatus[server.port] = false
        }
    }
}