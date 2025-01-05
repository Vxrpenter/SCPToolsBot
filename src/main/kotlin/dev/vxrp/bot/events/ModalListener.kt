package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.ticket.TicketHandler
import dev.vxrp.bot.ticket.TicketStatus
import dev.vxrp.bot.ticket.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class ModalListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    private val logger = LoggerFactory.getLogger(ModalListener::class.java)

    init {
        api.listener<ModalInteractionEvent> { event ->
            if (event.modalId.startsWith("support_general")) {
                val handler = TicketHandler(api, config, translation, TicketType.GENERAL, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
                val child = handler.createTicket()
                respond(child, event)
            }

            if (event.modalId.startsWith("support_report")) {
                val handler = TicketHandler(api, config, translation, TicketType.REPORT, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
                val child = handler.createTicket()
                respond(child, event)
            }

            if (event.modalId.startsWith("support_error")) {
                val handler = TicketHandler(api, config, translation, TicketType.ERROR, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
                val child = handler.createTicket()
                respond(child, event)
            }

            if (event.modalId.startsWith("support_unban")) {
                val handler = TicketHandler(api, config, translation, TicketType.UNBAN, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
                val child = handler.createTicket()
                respond(child, event)
            }
            if (event.modalId.startsWith("support_complaint")) {
                var creator = "anonymous"
                if (event.modalId.split(":")[2] == "false") creator = event.user.id

                val handler = TicketHandler(api, config, translation, TicketType.COMPLAINT, TicketStatus.OPEN, creator, null, event.modalId, event.values)
                val child = handler.createTicket()
                respond(child, event)
            }

        }
    }

    private fun respond(child: ThreadChannel?, event: ModalInteractionEvent) {
        if (child == null) {
            logger.error("Modal Interaction Suspended, error suspected. Child channel could not correctly be returned")
            event.reply_("Modal Interaction suspended, error in interaction supsected").queue()
            return
        }

        event.reply_("", listOf(Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketCreatedTitle)
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketCreatedBody.replace("%channel%", child.asMention))
        })).setEphemeral(true).queue()
    }
}