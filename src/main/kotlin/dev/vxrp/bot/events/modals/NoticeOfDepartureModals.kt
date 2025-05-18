package dev.vxrp.bot.events.modals

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureManager
import dev.vxrp.bot.noticeofdeparture.handler.NoticeOfDepartureMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class NoticeOfDepartureModals(val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        if (event.modalId.startsWith("notice_of_departure_general")) {
            val date = event.values[0].asString
            val reason = event.values[1].asString

            try {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                LocalDate.parse(date, formatter)
            } catch (_: DateTimeParseException) {
                event.hook.send("Please enter a valid date format to proceed").queue()
                return
            }

            // Get current date a day ago to allow for the current day to be selected and to allow different timezones
            // to be taken into account
            val currentDate = LocalDate.now().minusDays(1)
            val parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            if (parsedDate.isBefore(currentDate)) {
                event.hook.send("Please enter a date in the future to proceed").queue()
                return
            }

            NoticeOfDepartureMessageHandler(event.jda, config, translation).sendDecisionMessage(event.user.id, date, reason)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionSentTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionSentBody)
            }

            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
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