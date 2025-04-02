package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.ApplicationTable
import dev.vxrp.database.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TicketSettingsHandler(val api: JDA, val config: Config, val translation: Translation) {
    val logger: Logger = LoggerFactory.getLogger(TicketSettingsHandler::class.java)

    suspend fun claimTicket(user: User, ticketChannel: ThreadChannel, id: String, userId: String) {
        val embed = Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketClaimedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketClaimedBody
                .replace("%user%", user.asMention))
        }
        ticketChannel.send("", listOf(embed)).queue()

        val handlerUser = api.retrieveUserById(userId).await()

        TicketTable().updateTicketHandler(id, userId)
        TicketMessageHandler(api, config, translation).editMessage(id, ticketChannel)
        TicketLogHandler(api, config, translation).editMessage(id, handler = handlerUser)

        if (TicketTable().getTicketType(id) == TicketType.APPLICATION) {
            ApplicationTable().updateTicketHandler(id, handlerUser.id)
        }

        logger.info("Ticket {} claimed by user: {}", id, user.id)
    }

    suspend fun openTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketOpenedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketOpenedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        TicketMessageHandler(api, config, translation).editMessage(id, ticketChannel, ticketStatus = TicketStatus.OPEN)
        TicketLogHandler(api, config, translation).editMessage(id, ticketStatus = TicketStatus.OPEN)
        TicketTable().updateTicketStatus(id, TicketStatus.OPEN)
        val child = api.getThreadChannelById(id)!!
        child.manager.setLocked(false).queue()

        logger.info("Ticket {} opened by user: {}", id, user.id)
    }

    suspend fun pauseTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketPausedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketPausedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        TicketMessageHandler(api, config, translation).editMessage(id, ticketChannel, ticketStatus = TicketStatus.PAUSED)
        TicketLogHandler(api, config, translation).editMessage(id, ticketStatus = TicketStatus.PAUSED)
        TicketTable().updateTicketStatus(id, TicketStatus.PAUSED)
        val child = api.getThreadChannelById(id)!!
        child.manager.setLocked(true).queue()

        logger.info("Ticket {} paused by user: {}", id, user.id)
    }

    suspend fun suspendTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketSuspendedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketSuspendedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        TicketMessageHandler(api, config, translation).editMessage(id, ticketChannel, ticketStatus = TicketStatus.SUSPENDED)
        TicketLogHandler(api, config, translation).editMessage(id, ticketStatus = TicketStatus.SUSPENDED)
        TicketTable().updateTicketStatus(id, TicketStatus.SUSPENDED)
        val child = api.getThreadChannelById(id)!!
        child.manager.setLocked(true).queue()

        logger.info("Ticket {} suspended by user: {}", id, user.id)
    }

    suspend fun archiveTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        TicketLogHandler(api, config, translation).deleteMesssage(id)
        TicketTable().updateTicketStatus(id, TicketStatus.CLOSED)
        val child = api.getThreadChannelById(id)!!

        child.manager.setLocked(true).queue()
        child.manager.setArchived(true).queue()

        if (TicketTable().getTicketType(id) == TicketType.APPLICATION) {
            ApplicationTable().delete(id)
        }

        logger.info("Ticket {} archived by user: {}", id, user.id)
    }

    fun settingsActionRow(status: TicketStatus): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        var open = Button.success("ticket_setting_open", translation.buttons.textSupportSettingsOpen).withEmoji(Emoji.fromFormatted("🚪"))
        var pause = Button.primary("ticket_setting_pause", translation.buttons.textSupportSettingsPause).withEmoji(Emoji.fromFormatted("🌙"))
        var suspend = Button.primary("ticket_setting_suspend", translation.buttons.textSupportSettingsSuspend).withEmoji(
            Emoji.fromFormatted("🔒"))
        var close = Button.danger("ticket_setting_close", translation.buttons.textSupportSettingsClose).withEmoji(Emoji.fromFormatted("🪫"))

        when(status) {
            TicketStatus.OPEN ->open = open.asDisabled()
            TicketStatus.PAUSED -> pause = pause.asDisabled()
            TicketStatus.SUSPENDED -> suspend = suspend.asDisabled()
            TicketStatus.CLOSED -> close = close.asDisabled()
        }

        rows.add(open)
        rows.add(pause)
        rows.add(suspend)
        rows.add(close)

        return rows
    }
}