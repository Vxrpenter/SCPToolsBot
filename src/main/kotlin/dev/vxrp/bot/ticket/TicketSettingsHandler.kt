package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TicketSettingsHandler(val api: JDA, val config: Config, val translation: Translation) {
    suspend fun claimTicket(user: User, ticketChannel: ThreadChannel, id: String, userId: String) {
        TicketLogHandler(api, config, translation).editMessage(id, handler = api.retrieveUserById(userId).await())
        updateTicketHandler(id, userId)
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketClaimedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketClaimedBody
                .replace("%user%", user.asMention))
        }

        ticketChannel.send("", listOf(embed)).queue()
    }

    suspend fun openTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        TicketLogHandler(api, config, translation).editMessage(id, ticketStatus = TicketStatus.OPEN)
        updateTicketStatus(id, TicketStatus.OPEN)
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketOpenedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketOpenedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
    }

    suspend fun pauseTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketPausedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketPausedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        val child = api.getThreadChannelById(id)!!
        child.manager.setLocked(true).queue()
        TicketLogHandler(api, config, translation).editMessage(id, ticketStatus = TicketStatus.PAUSED)
        updateTicketStatus(id, TicketStatus.PAUSED)
    }

    suspend fun suspendTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketSuspendedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketSuspendedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        val child = api.getThreadChannelById(id)!!
        child.manager.setLocked(true).queue()
        TicketLogHandler(api, config, translation).editMessage(id, ticketStatus = TicketStatus.SUSPENDED)
        updateTicketStatus(id, TicketStatus.SUSPENDED)
    }

    fun archiveTicket(user: User, ticketChannel: ThreadChannel, id: String) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedTitle
                .replace("%user%", user.globalName!!))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedBody)
        }
        ticketChannel.send("", listOf(embed)).queue()
        val child = api.getThreadChannelById(id)!!
        child.manager.setArchived(true).queue()
        TicketLogHandler(api, config, translation).deleteMesssage(id)
        updateTicketStatus(id, TicketStatus.CLOSED)
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

    private fun updateTicketStatus(ticketId: String, ticketStatus: TicketStatus) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[status] = ticketStatus.toString()
            }
        }
    }

    fun getTicketStatus(ticketId: String): TicketStatus? {
        var status: TicketStatus? = null
        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    status = TicketStatus.valueOf(it[TicketTable.Tickets.status])
                }
        }

        return status
    }

    private fun updateTicketHandler(ticketId: String, userId: String) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[handler] = userId
            }
        }
    }
}