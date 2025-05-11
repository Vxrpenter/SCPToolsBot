package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.events.buttons.*
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

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.HELP_BUTTONS).engage) HelpButtons(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.TICKET_BUTTONS).engage) TicketButtons(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.APPLICATION_BUTTONS).engage) ApplicationButtons(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.VERIFY_BUTTONS).engage) VerifyButtons(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.NOTICE_OF_DEPARTURE_BUTTONS).engage) NoticeOfDepartureButtons(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.BUTTON_LISTENER, LaunchOptionSectionType.REGULARS_BUTTONS).engage) RegularsButtons(event, config, translation).init()
        }
    }
}