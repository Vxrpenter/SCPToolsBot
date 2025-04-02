package dev.vxrp.bot.events.modals

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

class NoticeOfDepartureModals(val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.modalId.startsWith("notice_of_departure_general")) {
            val date = event.values[0].asString
            val reason = event.values[1].asString

            NoticeOfDepartureManager(event.jda, config, translation).sendDecisionMessage(event.user.id, date, reason)

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionSentBody)
            }

            event.reply_("", listOf(embed)).queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_ACCEPTING")) {
            val reason = event.values[0].asString

            NoticeOfDepartureManager(event.jda, config, translation).sendAcceptedMessage(reason)
            NoticeOfDepartureManager(event.jda, config, translation).sendNoticeMessage()

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptationSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptationSentBody)
            }

            event.reply_("", listOf(embed)).queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_DISMISSING")) {
            val reason = event.values[0].asString

            NoticeOfDepartureManager(event.jda, config, translation).sendDismissedMessage(reason)

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissingSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissingSentBody)
            }

            event.reply_("", listOf(embed)).queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_REVOKING")) {
            val reason = event.values[0].asString


        }
    }
}