package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.events.stringSelectMenus.TicketStringSelectMenus
import dev.vxrp.bot.modals.Support
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu

class StringSelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<StringSelectInteractionEvent> { event ->
            TicketStringSelectMenus(event, config, translation)
        }
    }
}