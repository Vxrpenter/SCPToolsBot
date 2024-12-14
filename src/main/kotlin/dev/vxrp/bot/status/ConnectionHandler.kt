package dev.vxrp.bot.status

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.secretlab.data.Server
import dev.vxrp.secretlab.data.ServerInfo
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.util.*

private val serverStatus = hashMapOf<Int, Boolean>()
private val reconnectAttempt = hashMapOf<Int, Int>()
private val mappedUniqueIdsForPort = hashMapOf<Int, String>()

private var retryFetchData = 0
private var apiStatus: Boolean? = null

class ConnectionHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(ConnectionHandler::class.java)

    fun postApiConnectionUpdate(api: JDA, status: Status, content: Pair<ServerInfo?, MutableMap<Int, Server>>?) {
        if (apiStatus == null) {
            apiStatus = content != null
        }

        if (content != null) {
            if (apiStatus == true) return
            content.first?.let { postConnectionEstablished(api, it) }
            apiStatus = true
            retryFetchData = 0
            logger.info("Regained connection to secretlab api")
        } else {
            if (apiStatus == false) return
            if (retryFetchData == status.retryToFetchData+1) return
            if (retryFetchData == status.retryToFetchData) {
                logger.error("Completely lost connection to the secretlab api. This is not normal behavior and may be caused by an expired api key or wrong account number.")
                postConnectionLost(api, status.retryToFetchData)
                retryFetchData += 1
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
        serverStatus.putIfAbsent(server.port, server.online)
        reconnectAttempt.putIfAbsent(server.port, 1)

        if (server.online) {
            if (serverStatus[server.port] == true) return
            postConnectionOnline(api, instance, info!!)
            reconnectAttempt[server.port] = 1
            serverStatus[server.port] = true
            logger.info("Connection to server ${instance.name} (${instance.serverPort}) regained")
        } else {
            if (serverStatus[server.port] == false) return

            if (reconnectAttempt[server.port]!! == instance.retries + 1) return
            if (reconnectAttempt[server.port]!! == instance.retries) {
                logger.warn("Completely lost connection to server - ${instance.name}. Server is probably offline/unreachable")

                mappedUniqueIdsForPort[server.port] = UUID.randomUUID().toString()
                postConnectionOffline(api, instance, info!!)
                reconnectAttempt[server.port] = instance.retries + 1
                serverStatus[server.port] = false
                return
            }
            logger.warn("Failed to query data from \"${instance.name}\", trying to reconnect ${ instance.retries - reconnectAttempt[server.port]!!} more times")
            reconnectAttempt[server.port] = reconnectAttempt[server.port]!! + 1
        }
    }

    private fun postConnectionEstablished(api: JDA, info: ServerInfo) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
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

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionLost(api: JDA, retry: Int) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
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

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionOnline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
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

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionOffline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
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

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }
}