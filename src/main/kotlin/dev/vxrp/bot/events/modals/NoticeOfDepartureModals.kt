package dev.vxrp.bot.events.modals

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureManager
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

class NoticeOfDepartureModals(val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        if (event.modalId.startsWith("notice_of_departure_general")) {
            val date = event.values[0].asString
            val reason = event.values[1].asString

            NoticeOfDepartureMessageHandler(event.jda, config, translation).sendDecisionMessage(event.user.id, date, reason)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_ACCEPTING")) {
            val splittetId = event.modalId.split(":")

            val reason = event.values[0].asString
            val userId = splittetId[1]
            val date = splittetId[2]

            NoticeOfDepartureMessageHandler(event.jda, config, translation).sendAcceptedMessage(reason, userId, date)
            NoticeOfDepartureManager(event.jda, config, translation).createNotice(reason, event.user.id, userId, date)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptationSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptationSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message?.delete()?.queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_DISMISSING")) {
            val splittetId = event.modalId.split(":")

            val reason = event.values[0].asString
            val userId = splittetId[1]

            NoticeOfDepartureMessageHandler(event.jda, config, translation).sendDismissedMessage(reason, userId)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissingSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissingSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message?.delete()?.queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_REVOKING")) {
            val splittetId = event.modalId.split(":")

            val reason = event.values[0].asString
            val userId = splittetId[1]
            val date = splittetId[2]

            NoticeOfDepartureManager(event.jda, config, translation).revokeNotice(reason, userId, date)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedRevokationSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedRevokationSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message?.delete()?.queue()
        }
    }
}