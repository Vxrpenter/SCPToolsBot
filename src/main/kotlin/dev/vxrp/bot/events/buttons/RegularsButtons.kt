package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.RegularsMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class RegularsButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        if (event.button.id?.startsWith("regulars_open_settings") == true) {
            val regularsMessageHandler = RegularsMessageHandler(event.jda, config, translation)

            event.reply_("", listOf(regularsMessageHandler.getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }
    }
}