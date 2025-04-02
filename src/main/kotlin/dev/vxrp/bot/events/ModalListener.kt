package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.events.entitySelectMenus.TicketEntitySelectMenus
import dev.vxrp.bot.events.modals.ApplicationModals
import dev.vxrp.bot.events.modals.NoticeOfDepartureModals
import dev.vxrp.bot.events.modals.TicketModals
import dev.vxrp.bot.ticket.TicketHandler
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class ModalListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    private val logger = LoggerFactory.getLogger(ModalListener::class.java)

    init {
        api.listener<ModalInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.MODAL_LISTENER, LaunchOptionSectionType.TICKET_MODALS).engage) TicketModals(logger, event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.MODAL_LISTENER, LaunchOptionSectionType.APPLICATION_MODALS).engage) ApplicationModals(event, config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.MODAL_LISTENER, LaunchOptionSectionType.NOTICE_OF_DEPARTURE_MODALS).engage) NoticeOfDepartureModals(event, config, translation)
        }
    }
}