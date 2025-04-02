package dev.vxrp.bot.events.buttons

import dev.vxrp.bot.modals.NoticeOfDepartureTemplateModals
import dev.vxrp.bot.noticeofdeparture.enums.ActionId
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class NoticeOfDepartureButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.button.id?.startsWith("file_notice_of_departure") == true) {
            event.replyModal(NoticeOfDepartureTemplateModals(translation).generalModal()).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_accept") == true) {
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(translation).reasonActionModal(ActionId.ACCEPTING, userId, endTime))
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_dismiss") == true) {
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(translation).reasonActionModal(ActionId.DISMISSING, userId, endTime))
        }
    }
}