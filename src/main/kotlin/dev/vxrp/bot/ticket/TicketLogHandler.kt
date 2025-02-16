package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.Instant

class TicketLogHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(TicketLogHandler::class.java)

    suspend fun logMessage(creator: String, handler: User?, ticketId: String, ticketStatus: TicketStatus, childChannel: ThreadChannel): String? {
        val logEmbed = createMessage(creator, handler, ticketStatus, childChannel)

        val channel = api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not sent ticket log message for ticket '{}'", ticketId)
            return null
        }

        var isHandled = false
        if (handler != null) isHandled = true

        return channel.send("", listOf(logEmbed)).addActionRow(
            logActionRow(ticketStatus, isHandled, ticketId)
        ).await().id
    }

    suspend fun editMessage(ticketId: String, creator: String? = null, handler: User? = null, ticketStatus: TicketStatus? = null) {
        var logMessage: String? = null

        var creatorId: String? = null
        var handlerId: String? = null
        var status: TicketStatus? = null

        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    logMessage = it[TicketTable.Tickets.logMessage]

                    creatorId = it[TicketTable.Tickets.creator]
                    handlerId = it[TicketTable.Tickets.handler]
                    status = TicketStatus.valueOf(it[TicketTable.Tickets.status])
                }
        }
        val child = api.getThreadChannelById(ticketId)
        var handlerUser: User? = null
        if (handlerId != null) handlerUser = api.retrieveUserById(handlerId!!).await()

        if (creator != null) creatorId = creator
        if (handler != null) handlerUser = handler
        if (ticketStatus != null) status = ticketStatus

        val logEmbed = createMessage(creatorId!!, handlerUser!!, status!!, child!!)

        val channel = api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not edit ticket log message for ticket '{}'", ticketId)
            return
        }

        var isHandled = false
        if (handlerId != null) isHandled = true

        channel.editMessage(logMessage!!, "", listOf(logEmbed)).setActionRow(
            logActionRow(status!!, isHandled, ticketId)
        ).queue()
    }

    suspend fun deleteMesssage(ticketId: String) {
        var logMessage: String? = null

        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    logMessage = it[TicketTable.Tickets.logMessage]
                }
        }

        val channel = api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not delete ticket log message for ticket '{}'", ticketId)
            return
        }

        channel.deleteMessageById(logMessage!!).await()
    }

    private suspend fun createMessage(ticketCreator: String, ticketHandler: User?, ticketStatus: TicketStatus, childChannel: ThreadChannel): MessageEmbed {
        var thumbnailUrl = "https://www.pngarts.com/files/4/Anonymous-Mask-Transparent-Images.png"
        var creatorUserMention = "anonymous"
        var creatorUserName = "anonymous"
        var handlerUserName = "none"

        if (ticketCreator != "anonymous") {
            val creatorUser = api.retrieveUserById(ticketCreator).await()
            creatorUserName = creatorUser.globalName!!
            thumbnailUrl = creatorUser.avatarUrl.toString()
            creatorUserMention = api.retrieveUserById(ticketCreator).await().asMention
        }
        if (ticketHandler != null) handlerUserName = ticketHandler.asMention

        return Embed {
            thumbnail = thumbnailUrl
            title = ColorTool().useCustomColorCodes(translation.support.embedLogTitle
                .replace("%name%", childChannel.name)
                .replace("%user%", creatorUserName))
            description = ColorTool().useCustomColorCodes(translation.support.embedLogBody
                .replace("%status%", ticketStatus.toString())
                .replace("%id%", childChannel.id)
                .replace("%creator%", creatorUserMention)
                .replace("%handler%", handlerUserName))
            timestamp = Instant.now()
        }
    }

    private fun logActionRow(status: TicketStatus, handler: Boolean, ticketId: String): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        var claim = Button.primary("ticket_log_claim:$ticketId", translation.buttons.textSupportLogClaim).withEmoji(
            Emoji.fromFormatted("📫"))
        var open = Button.success("ticket_log_open:$ticketId", translation.buttons.textSupportLogOpen).withEmoji(Emoji.fromFormatted("🚪"))
        var pause = Button.primary("ticket_log_pause:$ticketId", translation.buttons.textSupportLogPause).withEmoji(
            Emoji.fromFormatted("🌙"))
        var suspend = Button.primary("ticket_log_suspend:$ticketId", translation.buttons.textSupportLogSuspend).withEmoji(
            Emoji.fromFormatted("🔒"))
        var close = Button.danger("ticket_log_close:$ticketId", translation.buttons.textSupportLogClose).withEmoji(Emoji.fromFormatted("🪫"))

        if (handler) claim = claim.asDisabled()
        when(status) {
            TicketStatus.OPEN ->open = open.asDisabled()
            TicketStatus.PAUSED -> pause = pause.asDisabled()
            TicketStatus.SUSPENDED -> suspend = suspend.asDisabled()
            TicketStatus.CLOSED -> close = close.asDisabled()
        }

        rows.add(claim)
        rows.add(open)
        rows.add(pause)
        rows.add(suspend)
        rows.add(close)

        return rows
    }
}