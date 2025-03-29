package dev.vxrp.bot.events.buttons

import dev.vxrp.bot.modals.NoticeOfDepartureModals
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class NoticeOfDepartureButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.button.id?.startsWith("file_notice_of_departure") == true) {
            event.replyModal(NoticeOfDepartureModals(translation).generalModal()).queue()
        }
    }
}