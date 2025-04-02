package dev.vxrp.bot.events.modals

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
            event.reply_(ColorTool().useCustomColorCodes(translation.noticeOfDeparture.messageDescisionSent)).queue()
        }
    }
}