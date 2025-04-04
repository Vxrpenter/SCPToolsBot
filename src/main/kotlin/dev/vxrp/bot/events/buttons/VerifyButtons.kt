package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.UserTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class VerifyButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.button.id?.startsWith("verify_show_data") == true) {
            val verified = UserTable().exists(event.user.id)
            val steamId = UserTable().getSteamId(event.user.id)
            val timestamp = UserTable().getVerifyTime(event.user.id)

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.verify.embedDataTitle)
                description = ColorTool().useCustomColorCodes(translation.verify.embedDataBody
                    .replace("%verified%", verified.toString())
                    .replace("%steamId%", steamId)
                    .replace("%timestamp%", timestamp))
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("verify_delete") == true) {
            UserTable().delete(event.user.id)

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.verify.embedDeletionSentTitle)
                description = ColorTool().useCustomColorCodes(translation.verify.embedDeletionSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }
}