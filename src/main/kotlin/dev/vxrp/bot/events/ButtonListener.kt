package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.commands.commanexecutes.help.HelpCommand
import dev.vxrp.bot.modals.Support
import dev.vxrp.bot.ticket.TicketHandler
import dev.vxrp.bot.ticket.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

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

            }

            if (event.button.id?.startsWith("ticket_close") == true) {
                TicketHandler(api, config, translation)
                    .deleteTicket(event.channelId!!, TicketType.valueOf(event.button.id!!.split(":")[1]))
            }

            if (event.button.id?.startsWith("ticket_settings") == true) {

            }
        }
    }
}