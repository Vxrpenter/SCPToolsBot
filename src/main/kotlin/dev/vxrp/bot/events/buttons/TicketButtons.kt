package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.SupportModals
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.bot.ticket.TicketSettingsHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import java.time.Instant

class TicketButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        val api = event.jda

        if (event.button.id?.startsWith("anonymous_accept") == true) {
            val userId = event.button.id!!.split(":")[1]

            event.replyModal(SupportModals(translation).supportComplaintModal(userId, true)).queue()
        }

        if (event.button.id?.startsWith("anonymous_deny") == true) {
            val userId = event.button.id!!.split(":")[1]

            event.replyModal(SupportModals(translation).supportComplaintModal(userId, false)).queue()
        }

        if (event.button.id?.startsWith("ticket_claim") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClaimedTitle
                    .replace("%name%", event.channelId.toString()))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClaimedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).claimTicket(event.user, event.channel.asThreadChannel() ,event.channelId!!, event.user.id)
        }

        if (event.button.id?.startsWith("ticket_close") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClosedTitle
                    .replace("%name%", event.channelId.toString()))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClosedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).archiveTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_settings") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val ticketHandler = TicketSettingsHandler(api, config, translation)

            val settings = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedSettingsTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedSettingsBody)
                timestamp = Instant.now()
            }

            event.reply_("", listOf(settings)).addActionRow(
                ticketHandler.settingsActionRow(ticketHandler.getTicketStatus(event.channelId!!)!!)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("ticket_setting_open") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogOpenedTitle
                    .replace("%name%", event.channelId.toString()))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogOpenedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).openTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_pause") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogPausedTitle
                    .replace("%name%", event.channelId.toString()))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogPausedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).pauseTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_suspend") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogSuspendedTitle
                    .replace("%name%", event.channelId.toString()))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogSuspendedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).suspendTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_close") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClosedTitle
                    .replace("%name%", event.channelId.toString()))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClosedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).archiveTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_log_claim") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.channelId!!))) return

            val channelId = event.button.id!!.split(":")[1]
            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClaimedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClaimedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).claimTicket(event.user, channel, channelId, event.user.id)
        }

        if (event.button.id?.startsWith("ticket_log_open") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.channelId!!))) return

            val channelId = event.button.id!!.split(":")[1]
            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogOpenedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogOpenedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).openTicket(event.user, channel, channelId)
        }

        if (event.button.id?.startsWith("ticket_log_pause") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.channelId!!))) return

            val channelId = event.button.id!!.split(":")[1]
            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogPausedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogPausedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).pauseTicket(event.user, channel, channelId)
        }

        if (event.button.id?.startsWith("ticket_log_suspend") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.channelId!!))) return

            val channelId = event.button.id!!.split(":")[1]
            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogSuspendedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogSuspendedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).suspendTicket(event.user, channel, channelId)
        }

        if (event.button.id?.startsWith("ticket_log_close") == true) {
            if (!PermissionManager(config, translation).determinePermissions(event.user, PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.channelId!!))) return

            val channelId = event.button.id!!.split(":")[1]
            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClosedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClosedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).archiveTicket(event.user, channel, channelId)
        }
    }
}