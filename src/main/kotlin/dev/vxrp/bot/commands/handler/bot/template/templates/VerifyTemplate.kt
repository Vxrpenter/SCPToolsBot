package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class VerifyTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        val embed = Embed {
            thumbnail = event.guild?.iconUrl
            title = ColorTool().useCustomColorCodes(translation.verify.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.verify.embedTemplateBody)
        }

        event.channel.send("", listOf(embed)).setActionRow(
            Button.link(config.settings.verify.oauthLink, translation.buttons.textVerifyVerify),
            Button.secondary("verify_show_data", translation.buttons.textVerifyShowData),
            Button.danger("verify_delete", translation.buttons.textVerifyDelete)
        ).queue()

        event.reply("Created verify template").setEphemeral(true).queue()
    }
}