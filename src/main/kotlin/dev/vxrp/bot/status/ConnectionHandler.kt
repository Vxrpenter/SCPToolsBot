package dev.vxrp.bot.status

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.ConnectionTable
import dev.vxrp.database.tables.ConnectionTable.Connections
import dev.vxrp.api.sla.secretlab.data.Server
import dev.vxrp.api.sla.secretlab.data.ServerInfo
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory

private val reconnectAttempt = hashMapOf<Int, Int>()

private var retryFetchData = 0

class ConnectionHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(ConnectionHandler::class.java)

    fun postApiConnectionUpdate(api: JDA, status: Status, content: Pair<ServerInfo?, MutableMap<Int, Server>>?) {
        val apiStatus = ConnectionTable().queryFromTable("api").status == true

        if (content != null) databaseNotExists("api", true)
        else databaseNotExists("api", false)

        if (content != null) {
            if (apiStatus) return
            content.first?.let { postConnectionEstablished(api, it) }
            postConnectionToDatabase("api", true)
            retryFetchData = 0
            logger.info("Regained connection to secretlab api")
        } else {
            if (!apiStatus) return
            if (retryFetchData == status.retryToFetchData+1) return
            if (retryFetchData == status.retryToFetchData) {
                logger.error("Completely lost connection to the secretlab api. This is not normal behavior and may be caused by an expired api key or wrong account number.")
                postConnectionLost(api, status.retryToFetchData)
                retryFetchData += 1
                postConnectionToDatabase("api", false)
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
        databaseNotExists(server.port.toString(), server.online)
        reconnectAttempt.putIfAbsent(server.port, 1)

        val serverStatus = ConnectionTable().queryFromTable(server.port.toString()).status == true

        if (server.online) {
            if (serverStatus) return
            postConnectionOnline(api, instance, info!!)
            reconnectAttempt[server.port] = 1
            postConnectionToDatabase(server.port.toString(), true)
            logger.info("Connection to server ${instance.name} (${instance.serverPort}) regained")
        } else {
            if (!serverStatus) return

            if (reconnectAttempt[server.port]!! == instance.retries + 1) return
            if (reconnectAttempt[server.port]!! == instance.retries) {
                logger.warn("Completely lost connection to server - ${instance.name}. Server is probably offline/unreachable")

                postConnectionOffline(api, instance, info!!)
                reconnectAttempt[server.port] = instance.retries + 1
                postConnectionToDatabase(server.port.toString(), false)
                return
            }
            logger.warn("Failed to query data from \"${instance.name}\", trying to reconnect ${ instance.retries - reconnectAttempt[server.port]!!} more times")
            reconnectAttempt[server.port] = reconnectAttempt[server.port]!! + 1
        }
    }

    private fun postConnectionEstablished(api: JDA, info: ServerInfo) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.settings.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedEstablishedTitle)
                .replace("%instance%", "Status Server System").trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedEstablishedBody).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldValue
                    .replace("%time%", "${info.response}")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedReasonFieldValue).trimIndent()
            }
        }

        if (config.settings.status.postServerStatus) {
            api.getTextChannelById(config.settings.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionLost(api: JDA, retry: Int) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.settings.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedLostTitle)
                .replace("%instance%", "Status Server System").trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedLostBody
                    .replace("%retries%", "$retry")
            ).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldValue
                    .replace("%time%", "999")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedLostReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedLostReasonFieldValue).trimIndent()
            }
        }

        if (config.settings.status.postServerStatus) {
            api.getTextChannelById(config.settings.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionOnline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.settings.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedOnlineTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedOnlineBody).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOnlineResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOnlineResponseFieldValue
                    .replace("%time%", "${info?.response}")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOnlineReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOnlineReasonFieldValue).trimIndent()
            }
        }

        if (config.settings.status.postServerStatus) {
            api.getTextChannelById(config.settings.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionOffline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.settings.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedOfflineTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedOfflineBody
                    .replace("%retries%", instance.retries.toString())
            ).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOfflineResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOfflineResponseFieldValue
                    .replace("%time%", "${info?.response}")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOfflineReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOfflineReasonFieldValue).trimIndent()
            }
        }

        if (config.settings.status.postServerStatus) {
            api.getTextChannelById(config.settings.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    // Database handler functions for easier usage
    private fun databaseNotExists(key: String, serverStatus: Boolean) {
        transaction {
            val exists = Connections.selectAll()
                .where { Connections.id.eq(key) }.empty()

            if (exists) {
                Connections.insert {
                    it[id] = key
                    it[status] = serverStatus
                }
            }
        }
    }

    private fun postConnectionToDatabase(key: String, serverStatus: Boolean) {
        transaction {
            Connections.update({Connections.id eq key}) {
                it[status] = serverStatus
            }
        }
    }
}