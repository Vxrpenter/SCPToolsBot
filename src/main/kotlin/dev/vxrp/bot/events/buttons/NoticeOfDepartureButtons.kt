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

package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.NoticeOfDepartureTemplateModals
import dev.vxrp.bot.noticeofdeparture.enums.ActionId
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class NoticeOfDepartureButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        if (event.button.id?.startsWith("notice_of_departure_file") == true) {
            event.replyModal(NoticeOfDepartureTemplateModals(config, translation).generalModal()).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_accept") == true) {
            if(permissionCheck(PermissionType.NOTICE_OF_DEPARTURES)) return
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(config, translation).reasonActionModal(ActionId.ACCEPTING, userId, endTime)).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_dismiss") == true) {
            if(permissionCheck(PermissionType.NOTICE_OF_DEPARTURES)) return
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(config, translation).reasonActionModal(ActionId.DISMISSING, userId, endTime)).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_revoke") == true) {
            if(permissionCheck(PermissionType.NOTICE_OF_DEPARTURES)) return
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(config, translation).reasonActionModal(ActionId.REVOKING, userId, endTime)).queue()
        }
    }

    private suspend fun permissionCheck(permissionType: PermissionType): Boolean {
        val permissionPair = PermissionManager(config, translation).determinePermissions(event.user, permissionType, null)
        if (permissionPair.second != null) {
            event.reply_("", listOf(permissionPair.second!!)).setEphemeral(true).queue()
        }
        if (!permissionPair.first) return true
        return false
    }
}