/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.commands.handler.bot.noticeofdeparture

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.NoticeOfDepartureTemplateModals
import dev.vxrp.bot.noticeofdeparture.enums.ActionId
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoticeOfDepartureCommand(val config: Config,  val translation: Translation) {
    fun view(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (!checkExistence(event, user)) return
        val handler = NoticeOfDepartureTable().retrieveHandler(user.id)!!
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern(config.settings.noticeOfDeparture.dateFormatting))
        val endDate = NoticeOfDepartureTable().retrieveEndDate(user.id)!!

        val embed = Embed {
            title = ColorTool().parse(translation.noticeOfDeparture.embedNoticeViewTitle
                .replace("%user%", user.name))
            description = ColorTool().parse(translation.noticeOfDeparture.embedNoticeViewBody
                .replace("%user%", "<@$handler>")
                .replace("%current_date%", currentDate)
                .replace("%end_date%", endDate))
        }

        event.reply_("", listOf(embed)).setEphemeral(true).queue()
    }

    fun revoke(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (!checkExistence(event, user)) return

        val endDate = NoticeOfDepartureTable().retrieveEndDate(user.id)!!
        event.replyModal(NoticeOfDepartureTemplateModals(config, translation).reasonActionModal(ActionId.REVOKING, user.id, endDate)).queue()
    }

    private fun checkExistence(event: SlashCommandInteractionEvent, user: User): Boolean {
        if (user.isBot || !NoticeOfDepartureTable().exists(user.id)) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().parse(translation.permissions.embedNotFoundTitle)
                description = ColorTool().parse(translation.permissions.embedNotFoundBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            return false
        }
        return true
    }
}