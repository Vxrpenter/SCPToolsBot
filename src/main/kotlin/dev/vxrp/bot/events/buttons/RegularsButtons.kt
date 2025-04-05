package dev.vxrp.bot.events.buttons

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class RegularsButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {

    }
}