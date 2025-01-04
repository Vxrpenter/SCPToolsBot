package dev.vxrp.bot.events.executes

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
            if (event.modalId == "support:general") {
                val ticketHandler = TicketHandler(api, config, TicketType.GENERAL, TicketStatus.OPEN, event.user, null)
                ticketHandler.createTicket()
            }

            if (event.modalId == "support:report") {
                val ticketHandler = TicketHandler(api, config, TicketType.REPORT, TicketStatus.OPEN, event.user, null)
                ticketHandler.createTicket()
            }

            if (event.modalId == "support:error") {
                val ticketHandler = TicketHandler(api, config, TicketType.ERROR, TicketStatus.OPEN, event.user, null)
                ticketHandler.createTicket()
            }

            if (event.modalId == "support:unban") {
                val ticketHandler = TicketHandler(api, config, TicketType.UNBAN, TicketStatus.OPEN, event.user, null)
                ticketHandler.createTicket()
            }

            if (event.modalId == "support:complaint") {
                val ticketHandler = TicketHandler(api, config, TicketType.COMPLAINT, TicketStatus.OPEN, event.user, null)
                ticketHandler.createTicket()
            }
        }
    }
}