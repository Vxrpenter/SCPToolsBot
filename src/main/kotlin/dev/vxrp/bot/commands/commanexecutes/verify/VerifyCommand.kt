package dev.vxrp.bot.commands.commanexecutes.verify

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class VerifyCommand(val config: Config, val translation: Translation) {
    fun pasteVerifyMenu(event: SlashCommandInteractionEvent) {
        if (!config.settings.webserver.active) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendPanelTitle)
                description = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendPanelBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            return
        }

        val embed = Embed {
            thumbnail = event.guild?.iconUrl
            title = ColorTool().useCustomColorCodes(translation.verify.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.verify.embedTemplateBody)
        }

        event.reply_("", listOf(embed)).setActionRow(
            Button.link(config.settings.verify.oauthLink, translation.buttons.textVerifyVerify),
            Button.secondary("verify_show_data", translation.buttons.textVerifyShowData),
            Button.danger("verify_delete", translation.buttons.textVerifyDelete)
        ).setEphemeral(true).queue()
    }
}