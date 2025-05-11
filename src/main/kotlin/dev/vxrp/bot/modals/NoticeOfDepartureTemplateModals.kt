package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.bot.noticeofdeparture.enums.ActionId
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class NoticeOfDepartureTemplateModals(val translation: Translation) {
    fun generalModal(): Modal {
        return Modal.create("notice_of_departure_general", translation.noticeOfDeparture.modalTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "notice_of_departure_general_time",
                    label = translation.noticeOfDeparture.modalTimeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.noticeOfDeparture.modalTimePlaceHolder).build()
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

    fun reasonActionModal(actionId: ActionId, userId: String, endTime: String): Modal {
        return Modal.create("notice_of_departure_reason_action_$actionId:$userId:$endTime", translation.noticeOfDeparture.modalReasonActionTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "notice_of_departure_reason_action_reason",
                    label = translation.noticeOfDeparture.modalReasonActionReasonTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.noticeOfDeparture.modalReasonActionPlaceholder).build()
            ),
        ).build()
    }
}