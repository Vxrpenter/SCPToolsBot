package dev.vxrp.bot.status

import dev.minn.jda.ktx.messages.editMessage
import dev.vxrp.bot.commands.commanexecutes.status.Playerlist
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.StatusTable
import dev.vxrp.api.sla.secretlab.data.Server
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PlayerlistHandler(val translation: Translation) {
    private val logger = LoggerFactory.getLogger(PlayerlistHandler::class.java)

    fun updatePlayerLists(ports: MutableMap<Int, Server>, instances: List<Instance>, instanceApiMap: MutableMap<Instance, JDA>, mappedStatusConstructor: MutableMap<Int, StatusConstructor>) {
        for (port in ports) {
            var api: JDA? = null

            for (instance in instances) {
                if (instance.serverPort == port.key) {
                    api = instanceApiMap[instance]
                    break
                }
            }

            transaction {
                StatusTable.Status.select(StatusTable.Status.channelId, StatusTable.Status.messageId)
                    .where { StatusTable.Status.port.eq(port.key.toString()) }
                    .forEach {
                        if (api == null) return@transaction
                        val embeds = mutableListOf<MessageEmbed>()
                        mappedStatusConstructor[port.key]?.let { statusConst ->
                            Playerlist().getEmbed(api.selfUser.id, translation, statusConst)
                        }?.let { playerListEmbed ->
                            embeds.add(playerListEmbed)
                        }

                        try {

                            api.getTextChannelById(it[StatusTable.Status.channelId])
                                ?.editMessage(it[StatusTable.Status.messageId], null, embeds)?.complete()
                        } catch (e: ErrorResponseException) {
                            StatusTable.Status.deleteWhere { StatusTable.Status.port.eq(port.key.toString()) }
                        }

                        logger.debug("Updated playerlist with message id: ${it[StatusTable.Status.messageId]} in channel ${it[StatusTable.Status.channelId]} part of server ${port.key}")
                    }
            }
        }
    }
}