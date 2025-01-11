package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.commanexecutes.help.HelpCommand
import dev.vxrp.bot.modals.Support
import dev.vxrp.bot.ticket.TicketHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.time.Instant

class ButtonListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<ButtonInteractionEvent> { event ->
            if (event.button.id?.startsWith("help_first_page") == true) {
                event.deferEdit().queue {
                    event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages().first())
                        .setActionRow(HelpCommand(translation).actionRow(0)).queue()
                }
            }

            if (event.button.id?.startsWith("help_last_page") == true) {
                event.deferEdit().queue {
                    event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages().last())
                        .setActionRow(HelpCommand(translation).actionRow(5)).queue()
                }
            }

            if (event.button.id?.startsWith("help_go_back") == true) {
                val page =
                    event.componentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt() - 1
                event.deferEdit().queue {
                    event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages()[page])
                        .setActionRow(HelpCommand(translation).actionRow(page)).queue()
                }
            }

            if (event.button.id?.startsWith("help_go_forward") == true) {
                val page =
                    event.componentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt() + 1
                event.deferEdit().queue {
                    event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages()[page])
                        .setActionRow(HelpCommand(translation).actionRow(page)).queue()
                }
            }

            if (event.button.id?.startsWith("anonymous_accept") == true) {
                val userId = event.button.id!!.split(":")[1]

                event.replyModal(Support(translation).supportComplaintModal(userId, true)).queue()
            }

            if (event.button.id?.startsWith("anonymous_deny") == true) {
                val userId = event.button.id!!.split(":")[1]

                event.replyModal(Support(translation).supportComplaintModal(userId, false)).queue()
            }

            if (event.button.id?.startsWith("ticket_claim") == true) {
                TicketHandler(api, config, translation).claimTicket(event.channelId!!, event.user.id)
            }

            if (event.button.id?.startsWith("ticket_close") == true) {
                TicketHandler(api, config, translation).archiveTicket(event.channelId!!)
            }

            if (event.button.id?.startsWith("ticket_settings") == true) {
                val ticketHandler = TicketHandler(api, config, translation)

                val settings = Embed {
                    title = ColorTool().useCustomColorCodes(translation.support.embedSettingsTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedSettingsBody)
                    timestamp = Instant.now()
                }

                event.reply_("", listOf(settings)).addActionRow(
                    ticketHandler.settingsActionRow(ticketHandler.getTicketStatus(event.channelId!!)!!)
                ).setEphemeral(true).queue()
            }

            if (event.button.id?.startsWith("ticket_setting_open") == true) {
                TicketHandler(api, config, translation).openTicket(event.channelId!!)
            }

            if (event.button.id?.startsWith("ticket_setting_pause") == true) {
                TicketHandler(api, config, translation).pauseTicket(event.channelId!!)
            }

            if (event.button.id?.startsWith("ticket_setting_suspend") == true) {
                TicketHandler(api, config, translation).suspendTicket(event.channelId!!)
            }

            if (event.button.id?.startsWith("ticket_setting_close") == true) {
                TicketHandler(api, config, translation).archiveTicket(event.channelId!!)
            }

            if (event.button.id?.startsWith("ticket_log_claim") == true) {
                val channelId = event.button.id!!.split(":")[1]
                TicketHandler(api, config, translation).claimTicket(channelId, event.user.id)
            }

            if (event.button.id?.startsWith("ticket_log_open") == true) {
                val channelId = event.button.id!!.split(":")[1]
                TicketHandler(api, config, translation).openTicket(channelId)
            }

            if (event.button.id?.startsWith("ticket_log_pause") == true) {
                val channelId = event.button.id!!.split(":")[1]
                TicketHandler(api, config, translation).pauseTicket(channelId)
            }

            if (event.button.id?.startsWith("ticket_log_suspend") == true) {
                val channelId = event.button.id!!.split(":")[1]
                TicketHandler(api, config, translation).suspendTicket(channelId)
            }

            if (event.button.id?.startsWith("ticket_log_close") == true) {
                val channelId = event.button.id!!.split(":")[1]
                TicketHandler(api, config, translation).archiveTicket(channelId)
            }
        }
    }
}