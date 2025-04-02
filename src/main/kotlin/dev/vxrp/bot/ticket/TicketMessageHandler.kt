package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.modals.ModalMapping
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.Instant

class TicketMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    val logger = LoggerFactory.getLogger(TicketMessageHandler::class.java)

    suspend fun sendMessage(type: TicketType, channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>): Message {
        when(type) {
            TicketType.GENERAL -> {
                return channel.send("", listOf(generalMessage(channel, userId, modalValues))).setActionRow(
                    messageActionRow(TicketStatus.OPEN, TicketType.GENERAL, false)
                ).await()
            }

            TicketType.REPORT -> {
                return channel.send("", listOf(reportMessage(channel, userId, modalId, modalValues))).setActionRow(
                    messageActionRow(TicketStatus.OPEN, TicketType.REPORT, false)
                ).await()
            }

            TicketType.ERROR -> {
                return channel.send("", listOf(errorMessage(channel, userId, modalValues))).setActionRow(
                    messageActionRow(TicketStatus.OPEN, TicketType.ERROR, false)
                ).await()
            }

            TicketType.UNBAN -> {
                return channel.send("", listOf(unbanMessage(channel, userId, modalValues))).setActionRow(
                    messageActionRow(TicketStatus.OPEN, TicketType.UNBAN, false)
                ).await()
            }

            TicketType.COMPLAINT -> {
                return channel.send("", listOf(complaintMessage(channel, userId, modalId, modalValues))).setActionRow(
                    messageActionRow(TicketStatus.OPEN, TicketType.COMPLAINT, false)
                ).await()
            }

            TicketType.APPLICATION -> {
                return channel.send("", listOf(applicationMessage(channel, userId, modalId, modalValues))).setActionRow(
                    messageActionRow(TicketStatus.OPEN, TicketType.APPLICATION, false)
                ).await()
            }
        }
    }
    fun editMessage(ticketId: String, ticketChannel: ThreadChannel, ticketStatus: TicketStatus? = null) {
        var message: String? = null

        var handlerId: String? = null
        var status: TicketStatus? = null
        var ticketType: TicketType? = null

        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    message = it[TicketTable.Tickets.message]
                    ticketType = TicketType.valueOf(it[TicketTable.Tickets.type])
                    handlerId = it[TicketTable.Tickets.handler]
                    status = TicketStatus.valueOf(it[TicketTable.Tickets.status])
                }
        }
        if (ticketStatus != null) status = ticketStatus

        var isHandled = false
        if (handlerId != null) isHandled = true

        ticketChannel.editMessage(message!!).setActionRow(
            messageActionRow(status!!, ticketType!!, isHandled)
        ).queue()
    }

    private suspend fun generalMessage(channel: ThreadChannel, userId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketGeneralTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketGeneralBody
                .replace("%issuerId%", userId)
                .replace("%subject%", modalValues[0].asString)
                .replace("%explanation%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun reportMessage(channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketReportTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketReportBody
                .replace("%issuerId%", userId)
                .replace("%reported%", "<@${modalId.split(":")[1]}>")
                .replace("%reason%", modalValues[0].asString)
                .replace("%proof%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun errorMessage(channel: ThreadChannel, userId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketErrorTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketErrorBody
                .replace("%issuerId%", userId)
                .replace("%problem%", modalValues[0].asString)
                .replace("%times%", modalValues[1].asString)
                .replace("%reproduce%", modalValues[2].asString)
                .replace("%additional%", modalValues[3].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun unbanMessage(channel: ThreadChannel, userId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketUnbanTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketUnbanBody
                .replace("%issuerId%", userId)
                .replace("%steamId%", modalValues[0].asString)
                .replace("%reason%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun complaintMessage(channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        var staff = "Anonymous"
        if (modalId.split(":")[1] != "anonymous") {
            staff = "<@${modalId.split(":")[1]}>"
        }

        return Embed {
            author {
                if (userId != "anonymous") {
                    val user = api.retrieveUserById(userId).await()
                    iconUrl = user.avatarUrl
                    name = user.globalName
                } else {
                    iconUrl = "https://www.pngarts.com/files/4/Anonymous-Mask-Transparent-Images.png"
                    name = "Anonymous"
                }
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketComplaintTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketComplaintBody
                .replace("%issuerId%", userId.replace("<@anonymous>", "**Anonymous**"))
                .replace("%staff%", staff)
                .replace("%reason%", modalValues[0].asString)
                .replace("%proof%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun applicationMessage(channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketApplicationTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketApplicationBody
                .replace("%issuerId%", userId)
                .replace("%roleId%", modalId.split(":")[1])
                .replace("%name%", modalValues[0].asString))
                .replace("%age%", modalValues[1].asString)
                .replace("%playtime%", modalValues[2].asString)
                .replace("%reason%", modalValues[3].asString)
                .replace("%skills%", modalValues[4].asString)
            timestamp = Instant.now()
        }
    }

    private fun messageActionRow(status: TicketStatus, type: TicketType, handler: Boolean): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        var claim = Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪"))
        var close = Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫"))
        val settings = Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))

        if (handler) claim = claim.asDisabled()
        if (status == TicketStatus.CLOSED) close = close.asDisabled()

        rows.add(claim)
        rows.add(close)
        rows.add(settings)

        return rows
    }
}