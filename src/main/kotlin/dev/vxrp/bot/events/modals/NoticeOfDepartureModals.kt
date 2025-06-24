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

package dev.vxrp.bot.events.modals

import dev.minn.jda.ktx.messages.Embed
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
        event.deferReply(true).queue()

        if (event.modalId.startsWith("notice_of_departure_general")) {
            val date = event.values[0].asString
            val reason = event.values[1].asString
            val formatter = DateTimeFormatter.ofPattern(config.settings.noticeOfDeparture.dateFormatting)

            val parsedDate = try {
                LocalDate.parse(date, formatter)
            } catch (_: DateTimeParseException) {
                val embed = Embed {
                    color = 0xE74D3C
                    title = ColorTool().parse(translation.noticeOfDeparture.embedEnterValidDateTitle)
                    description = ColorTool().parse(translation.noticeOfDeparture.embedEnterValidDateBody
                        .replace("%formatter%", config.settings.noticeOfDeparture.dateFormatting))
                }

                event.hook.send("", listOf(embed)).setEphemeral(true).queue()
                return
            }

            val currentDate = LocalDate.now()
            if (parsedDate.isBefore(currentDate) || parsedDate.isEqual(currentDate)) {
                val embed = Embed {
                    color = 0xE74D3C
                    title = ColorTool().parse(translation.noticeOfDeparture.embedEnterFutureDateTitle)
                    description = ColorTool().parse(translation.noticeOfDeparture.embedEnterFutureDateBody)
                }

                event.hook.send("", listOf(embed)).setEphemeral(true).queue()
                return
            }

            NoticeOfDepartureMessageHandler(event.jda, config, translation).sendDecisionMessage(event.user.id, date.format(formatter), reason)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.noticeOfDeparture.embedDecisionSentTitle)
                description = ColorTool().parse(translation.noticeOfDeparture.embedDecisionSentBody)
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
                title = ColorTool().parse(translation.noticeOfDeparture.embedAcceptationSentTitle)
                description = ColorTool().parse(translation.noticeOfDeparture.embedAcceptationSentBody)
            }

            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            event.message?.delete()?.queue()
        }

        if (event.modalId.startsWith("notice_of_departure_reason_action_DISMISSING")) {
            val splittetId = event.modalId.split(":")

            val reason = event.values[0].asString
            val userId = splittetId[1]

            NoticeOfDepartureMessageHandler(event.jda, config, translation).sendDismissedMessage(reason, userId)

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.noticeOfDeparture.embedDismissingSentTitle)
                description = ColorTool().parse(translation.noticeOfDeparture.embedDismissingSentBody)
            }

            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
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
                title = ColorTool().parse(translation.noticeOfDeparture.embedRevokationSentTitle)
                description = ColorTool().parse(translation.noticeOfDeparture.embedRevokationSentBody)
            }

            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
        }
    }
}