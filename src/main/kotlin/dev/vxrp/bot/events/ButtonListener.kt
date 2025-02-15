package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.events.buttons.ApplicationButtons
import dev.vxrp.bot.events.buttons.HelpButtons
import dev.vxrp.bot.events.buttons.TicketButtons
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ButtonListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<ButtonInteractionEvent> { event ->

            HelpButtons(event, config, translation)

            TicketButtons(event, config, translation).init()

            ApplicationButtons(event, config, translation).init()
        }
    }
}