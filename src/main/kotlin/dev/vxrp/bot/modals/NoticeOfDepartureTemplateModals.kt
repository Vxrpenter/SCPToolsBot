/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.bot.noticeofdeparture.enums.ActionId
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class NoticeOfDepartureTemplateModals(val config: Config, val translation: Translation) {
    fun generalModal(): Modal {
        return Modal.create("notice_of_departure_general", translation.noticeOfDeparture.modalTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "notice_of_departure_general_time",
                    label = translation.noticeOfDeparture.modalTimeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    requiredLength = 10..10,
                    placeholder = translation.noticeOfDeparture.modalTimePlaceHolder
                        .replace("%formatter%", config.settings.noticeOfDeparture.dateFormatting)).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "notice_of_departure_general_explanation",
                    label = translation.noticeOfDeparture.modalExplanationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    requiredLength = 4..2000,
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
                    requiredLength = 4..2000,
                    placeholder = translation.noticeOfDeparture.modalReasonActionPlaceholder).build()
            ),
        ).build()
    }
}