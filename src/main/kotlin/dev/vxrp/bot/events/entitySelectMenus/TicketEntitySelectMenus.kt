package dev.vxrp.bot.events.entitySelectMenus

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.SupportTemplateModals
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class TicketEntitySelectMenus(val event: EntitySelectInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.selectMenu.id?.startsWith("ticket_report") == true) {
            val user = event.values[0]
            event.replyModal(SupportTemplateModals(translation).supportReportModal(user.id)).queue()
        }

        if (event.selectMenu.id?.startsWith("ticket_complaint") == true) {
            val user = event.values[0]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedComplaintAnonymousTitle)
                description = ColorTool().useCustomColorCodes(translation.support.embedComplaintAnonymousBody)
            }

            event.reply_("", listOf(embed)).setActionRow(
                Button.success("ticket_anonymous_accept:${user.id}", translation.buttons.textSupportAnonymousAccept).withEmoji(
                    Emoji.fromFormatted("🔒")),
                Button.danger("ticket_anonymous_deny:${user.id}", translation.buttons.textSupportAnonymousDeny).withEmoji(Emoji.fromFormatted("🔓"))
            ).setEphemeral(true).queue()
        }
    }
}