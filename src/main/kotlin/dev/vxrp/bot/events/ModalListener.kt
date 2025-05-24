package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.events.modals.ApplicationModals
import dev.vxrp.bot.events.modals.NoticeOfDepartureModals
import dev.vxrp.bot.events.modals.TicketModals
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class ModalListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    private val logger = LoggerFactory.getLogger(ModalListener::class.java)

    init {
        api.listener<ModalInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.MODAL_LISTENER, LaunchOptionSectionType.TICKET_MODALS).engage && event.modalId.startsWith("ticket")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.ticketLogChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: TicketModals(logger, event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.MODAL_LISTENER, LaunchOptionSectionType.APPLICATION_MODALS).engage && event.modalId.startsWith("application")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.applicationMessageChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: ApplicationModals(event, config, translation)
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.MODAL_LISTENER, LaunchOptionSectionType.NOTICE_OF_DEPARTURE_MODALS).engage && event.modalId.startsWith("notice_of_departure")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.settings.noticeOfDeparture.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: NoticeOfDepartureModals(event, config, translation).init()
            }
        }
    }
}