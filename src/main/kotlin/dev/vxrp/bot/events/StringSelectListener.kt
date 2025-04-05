package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.events.stringSelectMenus.ApplicationStringSelectMenus
import dev.vxrp.bot.events.stringSelectMenus.RegularsStringSelectMenus
import dev.vxrp.bot.events.stringSelectMenus.TicketStringSelectMenus
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class StringSelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<StringSelectInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.STRING_SELECT_LISTENER, LaunchOptionSectionType.TICKET_STRING_SELECT_MENUS).engage) TicketStringSelectMenus(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.STRING_SELECT_LISTENER, LaunchOptionSectionType.APPLICATION_STRING_SELECT_MENUS).engage) ApplicationStringSelectMenus(event, config, translation).init()

            if (launchOptionManager.checkSectionOption(LaunchOptionType.STRING_SELECT_LISTENER, LaunchOptionSectionType.REGULARS_STRING_SELECT_MENUS).engage) RegularsStringSelectMenus(event, config, translation).init()
        }
    }
}