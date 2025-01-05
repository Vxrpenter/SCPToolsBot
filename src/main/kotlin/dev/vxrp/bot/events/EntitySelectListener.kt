package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button

class EntitySelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<EntitySelectInteractionEvent> { event ->
            if (event.selectMenu.id?.startsWith("report") == true) {
                val user = event.values[0]


            }

            if (event.selectMenu.id?.startsWith("complaint") == true) {
                val user = event.values[0]
                val embed = Embed {
                    title = ColorTool().useCustomColorCodes(translation.support.embedComplaintAnonymousTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedComplaintAnonymousBody)
                }

                event.reply_("", listOf(embed)).setActionRow(
                    Button.success("anonymous_accept:${user.id}", translation.buttons.textSupportAnonymousAccept).withEmoji(Emoji.fromFormatted("🔒")),
                    Button.danger("anonymous_deny:${user.id}", translation.buttons.textSupportAnonymousDeny).withEmoji(Emoji.fromFormatted("🔓"))
                ).setEphemeral(true).queue()
            }
        }
    }
}