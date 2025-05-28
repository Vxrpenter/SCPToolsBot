package dev.vxrp.bot.commands.handler.bot.noticeofdeparture

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.NoticeOfDepartureTemplateModals
import dev.vxrp.bot.noticeofdeparture.enums.ActionId
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoticeOfDepartureCommand(val config: Config,  val translation: Translation) {
    fun view(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (user.isBot) return
        val handler = NoticeOfDepartureTable().retrieveHandler(user.id)!!
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val endDate = NoticeOfDepartureTable().retrieveEndDate(user.id)

        if (endDate != null) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNoticeViewTitle
                    .replace("%user%", user.name))
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNoticeViewBody
                    .replace("%user%", "<@$handler>")
                    .replace("%current_date%", currentDate)
                    .replace("%end_date%", endDate))
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        } else {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNotFoundTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNotFoundBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }

    fun revoke(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (user.isBot) return
        val endDate = NoticeOfDepartureTable().retrieveEndDate(user.id)

        if (endDate != null) {
            event.replyModal(NoticeOfDepartureTemplateModals(translation).reasonActionModal(ActionId.REVOKING, user.id, endDate)).queue()
        } else {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNotFoundTitle)
                description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNotFoundBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }
}