package dev.vxrp.bot.events.modals

import dev.vxrp.bot.application.handler.ApplicationMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

class ApplicationModals(val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.modalId.startsWith("application_choose_count")) {
            val roleId = event.modalId.split(":")[1]
            val messageId = event.modalId.split(":")[2]
            val members = event.values[0].asString.toIntOrNull()

            event.deferEdit().queue()
            ApplicationMessageHandler(config, translation).editActivationMessage(event.user.id, roleId, event.channel.asTextChannel(), messageId, member = members, state = true, initializer = event.user.id)
        }
    }
}