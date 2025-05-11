package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.events.entitySelectMenus.TicketEntitySelectMenus
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EntitySelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<EntitySelectInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.ENTITY_SELECT_LISTENER, LaunchOptionSectionType.TICKET_ENTITY_SELECT_MENUS).engage) TicketEntitySelectMenus(event, config, translation)
        }
    }
}