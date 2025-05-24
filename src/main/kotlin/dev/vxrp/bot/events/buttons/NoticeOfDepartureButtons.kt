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
        if (event.button.id?.startsWith("file_notice_of_departure") == true) {
            event.replyModal(NoticeOfDepartureTemplateModals(translation).generalModal()).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_accept") == true) {
            if(permissionCheck(PermissionType.NOTICE_OF_DEPARTURES)) return
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(translation).reasonActionModal(ActionId.ACCEPTING, userId, endTime)).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_dismiss") == true) {
            if(permissionCheck(PermissionType.NOTICE_OF_DEPARTURES)) return
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(translation).reasonActionModal(ActionId.DISMISSING, userId, endTime)).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_revoke") == true) {
            if(permissionCheck(PermissionType.NOTICE_OF_DEPARTURES)) return
            val splittetId = event.button.id!!.split(":")

            val userId = splittetId[1]
            val endTime = splittetId[2]

            event.replyModal(NoticeOfDepartureTemplateModals(translation).reasonActionModal(ActionId.REVOKING, userId, endTime)).queue()
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