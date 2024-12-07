package dev.vxrp.bot.status

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.ConnectionLogsTable
import dev.vxrp.secretlab.data.Server
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.util.*

class ConnectionHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(ConnectionLogsTable::class.java)
    private val serverStatus = hashMapOf<Int, Boolean>()
    private val reconnectAttempt = hashMapOf<Int, Int>()
    private val mappedUniqueIdsForPort = hashMapOf<Int, String>()

    fun postStatusUpdate(server: Server, api: JDA, instance: Instance) {
        serverStatus.putIfAbsent(server.port, server.online)
        reconnectAttempt.putIfAbsent(server.port, 1)

        if (server.online) {
            if (serverStatus[server.port] == true) return
            postConnectionEstablished(api, instance)
            reconnectAttempt[server.port] = 1
            serverStatus[server.port] = true
            logger.info("Connection to server ${instance.name} (${instance.serverPort}) regained")
        } else {
            if (serverStatus[server.port] == false) return

            if (reconnectAttempt[server.port]!! == instance.retries + 1) return
            if (reconnectAttempt[server.port]!! == instance.retries) {
                logger.warn("Completely lost connection to server ${instance.name} (${instance.serverPort})")

                mappedUniqueIdsForPort[server.port] = UUID.randomUUID().toString()
                postConnectionLost(api, instance)
                reconnectAttempt[server.port] = instance.retries + 1
                serverStatus[server.port] = false
                return
            }
            logger.warn("Lost connection to server - ${instance.name} (${instance.serverPort}), trying reconnect... iteration ${reconnectAttempt[server.port]}")
            reconnectAttempt[server.port] = reconnectAttempt[server.port]!! + 1
        }
    }

    private fun postConnectionEstablished(api: JDA, instance: Instance) {
        val uuidForPort = mappedUniqueIdsForPort[instance.serverPort]

        transaction {
            ConnectionLogsTable.ConnectionLogs.update ({ ConnectionLogsTable.ConnectionLogs.id.eq(uuidForPort!!)}) {
                it[regainTime] = System.currentTimeMillis()
                it[concluded] = true
            }
        }

        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedEstablishedTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedEstablishedBody).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedFieldValue).trimIndent()
            }
        }

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionLost(api: JDA, instance: Instance) {
        transaction {
            ConnectionLogsTable.ConnectionLogs.insert {
                it[id] = mappedUniqueIdsForPort[instance.serverPort]!!
                it[port] = instance.serverPort
                it[lostTime] = System.currentTimeMillis()
            }
        }

        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedLostTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedLostBody
                    .replace("%retries%", instance.retries.toString())
            ).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedLostFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedLostFieldValue).trimIndent()
            }
        }

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }
}