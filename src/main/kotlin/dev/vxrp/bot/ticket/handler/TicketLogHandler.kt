package dev.vxrp.bot.ticket.handler

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.edit
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.TicketTable
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
        val logEmbed = createMessage(creator, handler, ticketStatus, childChannel, false)

        val channel = api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not sent ticket log message for ticket '{}'", ticketId)
            return null
        }

        var isHandled = false
        if (handler != null) isHandled = true

        return channel.send("", listOf(logEmbed)).addActionRow(logActionRow(ticketStatus, isHandled, ticketId)).await().id
    }

    suspend fun editMessage(ticketId: String, creator: String? = null, handler: User? = null, ticketStatus: TicketStatus? = null) {
        val logMessage = TicketTable().getLogMessage(ticketId)
        var creatorId = TicketTable().getTicketCreator(ticketId)
        val handlerId = TicketTable().getTicketHandler(ticketId)
        var status = TicketTable().getTicketStatus(ticketId)


        val child = api.getThreadChannelById(ticketId)
        var handlerUser: User? = null
        if (handlerId != null) handlerUser = api.retrieveUserById(handlerId!!).await()

        if (creator != null) creatorId = creator
        if (handler != null) handlerUser = handler
        if (ticketStatus != null) status = ticketStatus

        val logEmbed = createMessage(creatorId!!, handlerUser!!, status!!, child!!, false)

        val channel = api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not edit ticket log message for ticket '{}'", ticketId)
            return
        }

        var isHandled = false
        if (handlerId != null) isHandled = true

        channel.editMessage(logMessage!!, "", listOf(logEmbed)).setActionRow(logActionRow(status, isHandled, ticketId)).queue()
    }

    suspend fun closeMessage(ticketId: String, closedUser: User) {
        val logMessage = TicketTable().getLogMessage(ticketId)

        val creator = TicketTable().getTicketCreator(ticketId)!!
        val handler = api.retrieveUserById(TicketTable().getTicketHandler(ticketId)!!).await()
        val childChannel = api.getThreadChannelById(ticketId)!!

        val channel = api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not delete ticket log message for ticket '{}'", ticketId)
            return
        }

        val message = channel.retrieveMessageById(logMessage!!).await()
        message.editMessageComponents().queue()
        message.edit("", listOf(createMessage(creator, handler, TicketStatus.CLOSED, childChannel, true, closedUser = closedUser))).queue()
    }

    private suspend fun createMessage(ticketCreator: String, ticketHandler: User?, ticketStatus: TicketStatus, childChannel: ThreadChannel, closedMessage: Boolean, closedUser: User? = null): MessageEmbed {
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

        var usableColor = 0x2ECC70
        var usableTitle = ColorTool().useCustomColorCodes(translation.support.embedLogTitle
            .replace("%name%", childChannel.name)
            .replace("%user%", creatorUserName))
        var usableDescription = ColorTool().useCustomColorCodes(translation.support.embedLogBody
            .replace("%status%", ticketStatus.toString())
            .replace("%id%", childChannel.id)
            .replace("%creator%", creatorUserMention)
            .replace("%handler%", handlerUserName))

        if (closedMessage) {
            usableColor = 0xE74D3C
            usableTitle = ColorTool().useCustomColorCodes(translation.support.embedClosedLogTitle
                .replace("%name%", childChannel.name)
                .replace("%user%", creatorUserName))

            usableDescription = ColorTool().useCustomColorCodes(translation.support.embedClosedLogBody
                .replace("%status%", ticketStatus.toString())
                .replace("%id%", childChannel.id)
                .replace("%creator%", creatorUserMention)
                .replace("%handler%", handlerUserName)
                .replace("%closed_user%", closedUser?.asMention!!))
        }

        return Embed {
            color = usableColor
            thumbnail = thumbnailUrl
            title = usableTitle
            description = usableDescription
            timestamp = Instant.now()
        }
    }

    private fun logActionRow(status: TicketStatus, handler: Boolean, ticketId: String): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        var claim = Button.primary("ticket_log_claim:$ticketId", translation.buttons.textSupportLogClaim).withEmoji(Emoji.fromFormatted("📫"))
        var open = Button.success("ticket_log_open:$ticketId", translation.buttons.textSupportLogOpen).withEmoji(Emoji.fromFormatted("🚪"))
        var pause = Button.primary("ticket_log_pause:$ticketId", translation.buttons.textSupportLogPause).withEmoji(Emoji.fromFormatted("🌙"))
        var suspend = Button.primary("ticket_log_suspend:$ticketId", translation.buttons.textSupportLogSuspend).withEmoji(Emoji.fromFormatted("🔒"))
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