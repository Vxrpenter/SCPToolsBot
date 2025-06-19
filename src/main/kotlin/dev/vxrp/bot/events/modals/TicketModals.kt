package dev.vxrp.bot.events.modals

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.bot.ticket.TicketManager
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.bot.ticket.handler.TicketSettingsHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.ApplicationTable
import dev.vxrp.database.tables.database.ApplicationTypeTable
import dev.vxrp.database.tables.database.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import org.slf4j.Logger

class TicketModals(val logger: Logger, val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        event.deferReply(true).queue()

        val api = event.jda

        if (event.modalId.startsWith("ticket_general")) {
            val handler = TicketManager(api, config, translation)
            val child = handler.createTicket(TicketType.GENERAL, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("ticket_report")) {
            val handler = TicketManager(api, config, translation)
            val child = handler.createTicket(TicketType.REPORT, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("ticket_error")) {
            val handler = TicketManager(api, config, translation)
            val child = handler.createTicket(TicketType.ERROR, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("ticket_unban")) {
            val handler = TicketManager(api, config, translation)
            val child = handler.createTicket(TicketType.UNBAN, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("ticket_complaint")) {
            var creator = "anonymous"
            if (event.modalId.split(":")[2] == "false") creator = event.user.id

            val handler = TicketManager(api, config, translation)
            val child = handler.createTicket(TicketType.COMPLAINT, TicketStatus.OPEN, creator, null, event.modalId, event.values)
            respond(child, event)
        }

        if (event.modalId.startsWith("ticket_application")) {
            val roleId = event.modalId.split(":")[1]

            if (ApplicationTable().retrieveSerial(roleId) >= ApplicationTypeTable().query(roleId)!!.members!!) {
                val embed = Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.support.embedNoMoreApplicationsTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedNoMoreApplicationsBody
                        .replace("%members%", ApplicationTypeTable().query(roleId)!!.members!!.toString()))
                }
                event.hook.send("", listOf(embed)).setEphemeral(true).queue()
                return
            }

            if (event.values[1].asString.toIntOrNull() == null) {
                val embed = Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.support.embedApplicationAgeNumericTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedApplicationAgeNumericBody)
                }
                event.hook.send("", listOf(embed)).setEphemeral(true).queue()
                return
            }

            val handler = TicketManager(api, config, translation)
            val child = handler.createTicket(TicketType.APPLICATION, TicketStatus.OPEN, event.user.id, null, event.modalId, event.values)
            ApplicationTable().addToDatabase(child?.id.toString(), roleId, state =false, result = false, event.user.id, null)
            respond(child, event)
        }

        if (event.modalId.startsWith("ticket_close")) {
            val channelId = event.modalId.split(":")[1]
            val reason = event.values.first().asString
            if (TicketTable().determineHandler(channelId)) {
                event.hook.send("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.support.embedLogClosedTitle)
                description = ColorTool().useCustomColorCodes(translation.support.embedLogClosedBody
                    .replace("reason", reason))
            }
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).archiveTicket(event.user, channel, channelId, reason)
        }
    }

    private fun respond(child: ThreadChannel?, event: ModalInteractionEvent) {
        if (child == null) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.support.embedInteractionChainErrorTitle)
                description = ColorTool().useCustomColorCodes(translation.support.embedInteractionChainErrorBody)
            }
            logger.error("Modal Interaction Suspended, error suspected. Child channel could not correctly be returned")
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            return
        }

        event.hook.send("", listOf(Embed {
            color = 0x2ECC70
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketCreatedTitle)
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketCreatedBody.replace("%channel%", child.asMention))
        })).setEphemeral(true).queue()
    }

    private val noHandlerEmbed = Embed {
        title = ColorTool().useCustomColorCodes(translation.support.embedTicketNoHandlerTitle)
        description = ColorTool().useCustomColorCodes(translation.support.embedTicketNoHandlerBody)
    }
}