package dev.vxrp.bot.events.entitySelectMenus

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.Support
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class TicketEntitySelectMenus(val event: EntitySelectInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.selectMenu.id?.startsWith("report") == true) {
            val user = event.values[0]
            event.replyModal(Support(translation).supportReportModal(user.id)).queue()
        }

        if (event.selectMenu.id?.startsWith("complaint") == true) {
            val user = event.values[0]
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedComplaintAnonymousTitle)
                description = ColorTool().useCustomColorCodes(translation.support.embedComplaintAnonymousBody)
            }

            event.reply_("", listOf(embed)).setActionRow(
                Button.success("anonymous_accept:${user.id}", translation.buttons.textSupportAnonymousAccept).withEmoji(
                    Emoji.fromFormatted("🔒")),
                Button.danger("anonymous_deny:${user.id}", translation.buttons.textSupportAnonymousDeny).withEmoji(Emoji.fromFormatted("🔓"))
            ).setEphemeral(true).queue()
        }
    }
}