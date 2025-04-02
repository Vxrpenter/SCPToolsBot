package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.NoticeOfDepartureTemplateModals
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class NoticeOfDepartureButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.button.id?.startsWith("file_notice_of_departure") == true) {
            event.replyModal(NoticeOfDepartureTemplateModals(translation).generalModal()).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_accept") == true) {
            NoticeOfDepartureManager(event.jda, config, translation).sendAcceptedMessage()
            NoticeOfDepartureManager(event.jda, config, translation).sendNoticeMessage()

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptationSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptationSentBody)
            }

            event.reply_("", listOf(embed)).queue()
        }

        if (event.button.id?.startsWith("notice_of_departure_decision_dismiss") == true) {
            NoticeOfDepartureManager(event.jda, config, translation).sendDismissedMessage()

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissingSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissingSentBody)
            }

            event.reply_("", listOf(embed)).queue()
        }
    }
}