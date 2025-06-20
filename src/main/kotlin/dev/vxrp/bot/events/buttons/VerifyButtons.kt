package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.verify.VerifyMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.UserTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class VerifyButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        val noDataEmbed = Embed {
            color = 0xE74D3C
            title = ColorTool().parse(translation.verify.embedNoDataTitle)
            description = ColorTool().parse(translation.verify.embedNoDataBody)
        }

        if (event.button.id?.startsWith("verify_show_data") == true) {
            if (!UserTable().exists(event.user.id)) {
                event.reply_("", listOf(noDataEmbed)).setEphemeral(true).queue()
                return
            }

            var verified = "${UserTable().exists(event.user.id)}"
            if (verified == "true") {
                verified = "🟢 $verified"
            } else {
                verified = "🔴 $verified"
            }

            val steamId = UserTable().getSteamId(event.user.id)
            val timestamp = UserTable().getVerifyTime(event.user.id)

            val embed = Embed {
                thumbnail = event.user.avatarUrl
                title = ColorTool().parse(translation.verify.embedDataTitle)
                description = ColorTool().parse(translation.verify.embedDataBody)
                field {
                    inline = true
                    name = translation.verify.embedDataFieldVerifiedTitle
                    value = verified
                }
                field {
                    inline = true
                    name = translation.verify.embedDataFieldSteamIdTitle
                    value = steamId!!
                }
                field {
                    inline = true
                    name = translation.verify.embedDataFieldTimestampTitle
                    value = timestamp
                }
                field {
                    value = translation.verify.embedDataFieldDeleteValue
                }
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
                title = ColorTool().parse(translation.verify.embedDeletionSentTitle)
                description = ColorTool().parse(translation.verify.embedDeletionSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            VerifyMessageHandler(event.jda, config, translation).sendDeletionMessage(event.user)
        }
    }
}