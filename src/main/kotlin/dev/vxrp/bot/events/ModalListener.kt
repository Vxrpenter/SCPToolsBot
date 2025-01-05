package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.ticket.TicketHandler
import dev.vxrp.bot.ticket.TicketStatus
import dev.vxrp.bot.ticket.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ModalListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<ModalInteractionEvent> { event ->
            if (event.modalId.startsWith("support_general")) {
                val handler = TicketHandler(api, config, TicketType.GENERAL, TicketStatus.OPEN, event.user.id, null)
                handler.createTicket()
            }

            if (event.modalId.startsWith("support_report")) {
                val handler = TicketHandler(api, config, TicketType.REPORT, TicketStatus.OPEN, event.user.id, null)
                handler.createTicket()
            }

            if (event.modalId.startsWith("support_error")) {
                val handler = TicketHandler(api, config, TicketType.ERROR, TicketStatus.OPEN, event.user.id, null)
                handler.createTicket()
            }

            if (event.modalId.startsWith("support_unban")) {
                val handler = TicketHandler(api, config, TicketType.UNBAN, TicketStatus.OPEN, event.user.id, null)
                handler.createTicket()
            }
            if (event.modalId.startsWith("support_complaint")) {
                var creator = "anonymous"
                val anonymous: String = event.modalId.split(":")[1]
                if (anonymous == "false") creator = event.user.id

                val handler = TicketHandler(api, config, TicketType.COMPLAINT, TicketStatus.OPEN, creator, null)
                handler.createTicket()
            }

        }
    }
}