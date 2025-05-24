package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.events.buttons.*
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ButtonListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<ButtonInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.HELP_BUTTONS).engage && event.button.id!!.startsWith("help")) {
                HelpButtons(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.TICKET_BUTTONS).engage && event.button.id!!.startsWith("ticket")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.ticketLogChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: TicketButtons(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.APPLICATION_BUTTONS).engage && event.button.id!!.startsWith("application")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.applicationMessageChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: ApplicationButtons(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.VERIFY_BUTTONS).engage && event.button.id!!.startsWith("verify")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: VerifyButtons(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.NOTICE_OF_DEPARTURE_BUTTONS).engage && event.button.id!!.startsWith("notice_of_departure")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.settings.noticeOfDeparture.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: NoticeOfDepartureButtons(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.REGULARS_BUTTONS).engage && event.button.id!!.startsWith("regulars")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: RegularsButtons(event, config, translation).init()
            }
        }
    }
}