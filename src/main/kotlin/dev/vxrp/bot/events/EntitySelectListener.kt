package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.events.entitySelectMenus.TicketEntitySelectMenus
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EntitySelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<EntitySelectInteractionEvent> { event ->
            TicketEntitySelectMenus(event, config, translation)
        }
    }
}