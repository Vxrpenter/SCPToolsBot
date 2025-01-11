package dev.vxrp.bot.status

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.commanexecutes.status.Playerlist
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.StatusTable
import dev.vxrp.api.sla.secretlab.data.Server
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.tables.StatusTable.Status
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.LocalDate

class PlayerlistHandler(val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(PlayerlistHandler::class.java)

    suspend fun updatePlayerLists(ports: MutableMap<Int, Server>, instances: List<Instance>, instanceApiMap: MutableMap<Instance, JDA>, mappedStatusConstructor: MutableMap<Int, StatusConstructor>) {
        for (port in ports) {
            var api: JDA? = null

            for (instance in instances) {
                if (instance.serverPort != port.key) continue
                api = instanceApiMap[instance] ?: run {
                    logger.error("Could not retrieve mapped bot for port: {}", port)
                    return
                }
                break
            }
            createPresetMessage(api!!, port, mappedStatusConstructor)
            updateMessage(api, port, mappedStatusConstructor)
        }
    }

    private fun updateMessage(api: JDA, port: MutableMap.MutableEntry<Int, Server>, mappedStatusConstructor: MutableMap<Int, StatusConstructor>) {
        transaction {
            Status.select(Status.channelId, Status.messageId)
                .where { Status.port.eq(port.key.toString()) }
                .forEach {

                    val embeds = mutableListOf<MessageEmbed>()
                    mappedStatusConstructor[port.key]?.let { statusConst ->
                        Playerlist().getEmbed(api.selfUser.id, translation, statusConst)
                    }?.let { playerListEmbed ->
                        embeds.add(playerListEmbed)
                    }

                    try {
                        api.getTextChannelById(it[Status.channelId])
                            ?.editMessage(it[Status.messageId], null, embeds)?.complete()
                    } catch (e: ErrorResponseException) {
                        Status.deleteWhere { Status.port.eq(port.key.toString()) }
                    }

                    logger.debug("Updated playerlist with message id: ${it[Status.messageId]} in channel ${it[Status.channelId]} part of server ${port.key}")
                }

            Status.update({ Status.port.eq(port.key.toString()) }) {
                it[lastUpdated] =  System.currentTimeMillis().toString()
            }
        }
    }

    private suspend fun createPresetMessage(api: JDA, port: MutableMap.MutableEntry<Int, Server>, mappedStatusConstructor: MutableMap<Int, StatusConstructor>) {
        for (instance in config.status.instances) {
            if (instance.serverPort != port.key) continue
            if (!instance.playerlist.active) continue

            var present = false
            transaction {
                Status.selectAll()
                    .where { Status.type.eq(PlayerlistType.PRESET.toString()) }
                    .forEach {
                        if (it[Status.port] == port.key.toString()) {
                            logger.debug("Skipping over preset creation for server '{}'", instance.name)
                            present = true
                        }
                    }
            }
            if (present) return

            for (channelId in instance.playerlist.channelId) {
                val channel = api.getTextChannelById(channelId) ?: run {
                    logger.error("Could not find channel '{}' to paste preset playerlist of server '{}'", channelId, instance.name)
                    return
                }

                val embeds = mutableListOf<MessageEmbed>()
                mappedStatusConstructor[port.key]?.let { statusConst ->
                    Playerlist().getEmbed(api.selfUser.id, translation, statusConst)
                }?.let { playerListEmbed ->
                    embeds.add(playerListEmbed)
                }

                val message = channel.sendMessageEmbeds(embeds).await()

                transaction {
                    Status.insert {
                        it[type] = PlayerlistType.PRESET.toString()
                        it[Status.channelId] = channelId
                        it[messageId] = message.id
                        it[Status.port] = port.key.toString()
                        it[created] = LocalDate.now().toString()
                        it[lastUpdated] = System.currentTimeMillis().toString()
                    }
                }
            }
            break
        }
    }
}