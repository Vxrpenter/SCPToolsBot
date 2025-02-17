package dev.vxrp.bot.events.modals

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.ticket.TicketHandler
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.ApplicationTable
import dev.vxrp.database.tables.ApplicationTypeTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import org.slf4j.Logger

class TicketModals(val logger: Logger, val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        val api = event.jda

        if (event.modalId.startsWith("support_general")) {
            val handler = TicketHandler(api, config, translation)
            val child = handler.createTicket(TicketType.GENERAL, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("support_report")) {
            val handler = TicketHandler(api, config, translation)
            val child = handler.createTicket(TicketType.REPORT, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("support_error")) {
            val handler = TicketHandler(api, config, translation)
            val child = handler.createTicket(TicketType.ERROR, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("support_unban")) {
            val handler = TicketHandler(api, config, translation)
            val child = handler.createTicket(TicketType.UNBAN, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("support_complaint")) {
            var creator = "anonymous"
            if (event.modalId.split(":")[2] == "false") creator = event.user.id

            val handler = TicketHandler(api, config, translation)
            val child = handler.createTicket(TicketType.COMPLAINT, TicketStatus.OPEN, creator, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("support_application")) {
            val handler = TicketHandler(api, config, translation)
            val child = handler.createTicket(TicketType.APPLICATION, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            val roleId = event.modalId.split(":")[1]

            if (ApplicationTable().retrieveSerial(roleId) >= ApplicationTypeTable().query(roleId)!!.members!!) {
                event.reply_("No more applications can't be opened until another is closed").setEphemeral(true).queue()
                return
            }

            ApplicationTable().addToDatabase(child?.id.toString(), roleId, false, false, event.user.id, null)
            respond(child, event)
        }
    }

    private fun respond(child: ThreadChannel?, event: ModalInteractionEvent) {
        if (child == null) {
            logger.error("Modal Interaction Suspended, error suspected. Child channel could not correctly be returned")
            event.reply_("Modal Interaction suspended, error in interaction suspected").queue()
            return
        }

        event.reply_("", listOf(Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketCreatedTitle)
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketCreatedBody.replace("%channel%", child.asMention))
        })).setEphemeral(true).queue()
    }
}