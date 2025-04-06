package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.verify.VerifyMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.UserTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class VerifyButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        val noDataEmbed = Embed {
            title = ColorTool().useCustomColorCodes(translation.verify.embedNoDataTitle)
            description = ColorTool().useCustomColorCodes(translation.verify.embedNoDataBody)
        }

        if (event.button.id?.startsWith("verify_show_data") == true) {
            if (!UserTable().exists(event.user.id)) {
                event.reply_("", listOf(noDataEmbed)).setEphemeral(true).queue()
                return
            }

            val verified = UserTable().exists(event.user.id)
            val steamId = UserTable().getSteamId(event.user.id)
            val timestamp = UserTable().getVerifyTime(event.user.id)

            val embed = Embed {
                thumbnail = event.user.avatarUrl
                title = ColorTool().useCustomColorCodes(translation.verify.embedDataTitle)
                description = ColorTool().useCustomColorCodes(translation.verify.embedDataBody
                    .replace("%verified%", verified.toString())
                    .replace("%steamId%", steamId)
                    .replace("%timestamp%", timestamp))
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("verify_delete") == true) {
            if (!UserTable().exists(event.user.id)) {
                event.reply_("", listOf(noDataEmbed)).setEphemeral(true).queue()
                return
            }

            UserTable().delete(event.user.id)

            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.verify.embedDeletionSentTitle)
                description = ColorTool().useCustomColorCodes(translation.verify.embedDeletionSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            VerifyMessageHandler(event.jda, config, translation).sendDeletionMessage(event.user.name)
        }
    }
}