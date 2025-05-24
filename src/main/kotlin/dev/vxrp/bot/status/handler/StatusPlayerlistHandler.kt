package dev.vxrp.bot.status.handler

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.editMessage
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.bot.commands.handler.status.playerlist.PlayerlistMessageHandler
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.enums.PlayerlistType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.StatusTable
import io.github.vxrpenter.secretlab.data.Server
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import org.slf4j.LoggerFactory
import java.time.LocalDate

class StatusPlayerlistHandler(val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(StatusPlayerlistHandler::class.java)

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
        for (entry in StatusTable().getAllEntrys()) {
            if (entry.port == port.key.toString()) return

            val embeds = mutableListOf<MessageEmbed>()
            mappedStatusConstructor[port.key]?.let { statusConst ->
                PlayerlistMessageHandler().getEmbed(api.selfUser.id, translation, statusConst)
            }?.let { playerListEmbed ->
                embeds.add(playerListEmbed)
            }

            try {
                api.getTextChannelById(entry.channelId)
                    ?.editMessage(entry.messageId, null, embeds)?.complete()
            } catch (_: ErrorResponseException) {
                StatusTable().deleteFromDatabase(port.key.toString())
            }

            logger.debug("Updated playerlist with message id: ${entry.messageId} in channel ${entry.channelId} part of server ${port.key}")
        }

        StatusTable().updateLastUpdated(port.key.toString(), System.currentTimeMillis().toString())
    }

    private suspend fun createPresetMessage(api: JDA, port: MutableMap.MutableEntry<Int, Server>, mappedStatusConstructor: MutableMap<Int, StatusConstructor>) {
        for (instance in config.status.instances) {
            if (instance.serverPort != port.key) continue
            if (!instance.playerlist.active) continue

            val playerlistType = StatusTable().getType(port.key.toString())

            if (playerlistType != PlayerlistType.PRESET) {
                logger.debug("Skipping over preset creation for server '{}'", instance.name)
                return
            }

            for (channelId in instance.playerlist.channelId) {
                val channel = api.getTextChannelById(channelId) ?: run {
                    logger.error("Could not find channel '{}' to paste preset playerlist of server '{}'", channelId, instance.name)
                    return
                }

                val embeds = mutableListOf<MessageEmbed>()
                mappedStatusConstructor[port.key]?.let { statusConst ->
                    PlayerlistMessageHandler().getEmbed(api.selfUser.id, translation, statusConst)
                }?.let { playerListEmbed ->
                    embeds.add(playerListEmbed)
                }

                val message = channel.sendMessageEmbeds(embeds).await()

                StatusTable().addToDatabase(PlayerlistType.PRESET, channelId, message.id, port.key.toString(), LocalDate.now().toString(), System.currentTimeMillis().toString())
            }
            break
        }
    }
}