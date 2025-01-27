package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.Support
import dev.vxrp.bot.ticket.TicketSettingsHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import java.time.Instant

class TicketButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        val api = event.jda

        if (event.button.id?.startsWith("anonymous_accept") == true) {
            val userId = event.button.id!!.split(":")[1]

            event.replyModal(Support(translation).supportComplaintModal(userId, true)).queue()
        }

        if (event.button.id?.startsWith("anonymous_deny") == true) {
            val userId = event.button.id!!.split(":")[1]

            event.replyModal(Support(translation).supportComplaintModal(userId, false)).queue()
        }

        if (event.button.id?.startsWith("ticket_claim") == true) {
            TicketSettingsHandler(api, config, translation).claimTicket(event.channelId!!, event.user.id)
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedTicketClaimedTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedTicketClaimedBody
                    .replace("%user%", event.user.asMention))
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("ticket_close") == true) {
            TicketSettingsHandler(api, config, translation).archiveTicket(event.channelId!!)
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("ticket_settings") == true) {
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
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedTicketOpenedTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedTicketOpenedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).openTicket(event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_pause") == true) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedTicketPausedTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedTicketPausedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).pauseTicket(event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_suspend") == true) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedTicketSuspendedTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedTicketSuspendedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).suspendTicket(event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_close") == true) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().useCustomColorCodes(translation.support.embedTicketClosedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).archiveTicket(event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_log_claim") == true) {
            val channelId = event.button.id!!.split(":")[1]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClaimedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClaimedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).claimTicket(channelId, event.user.id)
        }

        if (event.button.id?.startsWith("ticket_log_open") == true) {
            val channelId = event.button.id!!.split(":")[1]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogOpenedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogOpenedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).openTicket(channelId)
        }

        if (event.button.id?.startsWith("ticket_log_pause") == true) {
            val channelId = event.button.id!!.split(":")[1]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogPausedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogPausedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).pauseTicket(channelId)
        }

        if (event.button.id?.startsWith("ticket_log_suspend") == true) {
            val channelId = event.button.id!!.split(":")[1]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogSuspendedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogSuspendedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).suspendTicket(channelId)
        }

        if (event.button.id?.startsWith("ticket_log_close") == true) {
            val channelId = event.button.id!!.split(":")[1]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClosedTitle
                    .replace("%name%", channelId))
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClosedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).archiveTicket(channelId)
        }
    }
}