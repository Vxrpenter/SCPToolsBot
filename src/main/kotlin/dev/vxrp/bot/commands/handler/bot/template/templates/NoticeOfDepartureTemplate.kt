package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class NoticeOfDepartureTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedTemplateBody)
        }

        event.channel.send("", listOf(embed)).setActionRow(
            Button.success("file_notice_of_departure", translation.buttons.textNoticeOfDepartureFile).withEmoji(Emoji.fromFormatted("⏰"))
        ).queue()

        event.reply("Created notice of departure template").setEphemeral(true).queue()
    }
}