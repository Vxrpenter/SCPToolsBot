package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class NoticeOfDepartureModals(val translation: Translation) {
    fun generalModal(): Modal {
        return Modal.create("notice_of_departure_general", translation.noticeOfDeparture.modalTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "notice_of_departure_general_time",
                    label = translation.noticeOfDeparture.modalTimeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.noticeOfDeparture.modalTimeTitle).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "notice_of_departure_general_explanation",
                    label = translation.noticeOfDeparture.modalExplanationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.noticeOfDeparture.modalExplanationPlaceHolder).build()
            )
        ).build()
    }
}