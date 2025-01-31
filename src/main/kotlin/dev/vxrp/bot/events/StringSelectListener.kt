package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.events.stringSelectMenus.ApplicationStringSelectMenus
import dev.vxrp.bot.events.stringSelectMenus.TicketStringSelectMenus
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class StringSelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<StringSelectInteractionEvent> { event ->
            TicketStringSelectMenus(event, config, translation)

            ApplicationStringSelectMenus(event, config, translation)
        }
    }
}