package dev.vxrp.bot.status

import dev.vxrp.api.sla.secretlab.data.Server
import dev.vxrp.api.sla.secretlab.data.ServerInfo
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.database.ConnectionTable
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory

private val reconnectAttempt = hashMapOf<Int, Int>()
private var retryFetchData = 0

class StatusConnectionHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(StatusConnectionHandler::class.java)

    fun postApiConnectionUpdate(api: JDA, status: Status, content: Pair<ServerInfo?, MutableMap<Int, Server>>?) {
        val apiStatus = ConnectionTable().queryFromTable("api").status == true

        if (content != null) ConnectionTable().databaseNotExists("api", true)
        else ConnectionTable().databaseNotExists("api", false)

        if (content != null) {
            if (apiStatus) return
            content.first?.let { StatusMessageHandler(config, translation).postConnectionEstablished(api, it) }
            ConnectionTable().postConnectionToDatabase("api", true)
            retryFetchData = 0
            logger.info("Regained connection to secretlab api")
        } else {
            if (!apiStatus) return
            if (retryFetchData == status.retryToFetchData+1) return
            if (retryFetchData == status.retryToFetchData) {
                logger.error("Completely lost connection to the secretlab api. This is not normal behavior and may be caused by an expired api key or wrong account number.")
                StatusMessageHandler(config, translation).postConnectionLost(api, status.retryToFetchData)
                retryFetchData += 1
                ConnectionTable().postConnectionToDatabase("api", false)
                return
            }
            if (retryFetchData >= status.suspectRateLimitUntil && retryFetchData != status.retryToFetchData+1) {
                logger.warn("Failed $retryFetchData consecutive times to connect to the api. Suspecting an api outage or invalid key. Retrying ${status.retryToFetchData- retryFetchData} more times")
            }
            if (retryFetchData < status.suspectRateLimitUntil) {
                logger.warn("Failed to access the secretlab api, suspecting rate limiting, retrying in ${status.checkRate} seconds")
            }
            retryFetchData += 1
        }
    }

    fun postStatusUpdate(server: Server, api: JDA, instance: Instance, info: ServerInfo?) {
        ConnectionTable().databaseNotExists(server.port.toString(), server.online)
        reconnectAttempt.putIfAbsent(server.port, 1)

        val serverStatus = ConnectionTable().queryFromTable(server.port.toString()).status == true

        if (server.online) {
            if (serverStatus) return
            StatusMessageHandler(config, translation).postConnectionOnline(api, instance, info!!)
            reconnectAttempt[server.port] = 1
            ConnectionTable().postConnectionToDatabase(server.port.toString(), true)
            logger.info("Connection to server ${instance.name} (${instance.serverPort}) regained")
        } else {
            if (!serverStatus) return

            if (reconnectAttempt[server.port]!! == instance.retries + 1) return
            if (reconnectAttempt[server.port]!! == instance.retries) {
                logger.warn("Completely lost connection to server - ${instance.name}. Server is probably offline/unreachable")

                StatusMessageHandler(config, translation).postConnectionOffline(api, instance, info!!)
                reconnectAttempt[server.port] = instance.retries + 1
                ConnectionTable().postConnectionToDatabase(server.port.toString(), false)
                return
            }
            logger.warn("Failed to query data from \"${instance.name}\", trying to reconnect ${ instance.retries - reconnectAttempt[server.port]!!} more times")
            reconnectAttempt[server.port] = reconnectAttempt[server.port]!! + 1
        }
    }
}