package dev.vxrp.bot.commands.commanexecutes.template

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class NoticeOfDepartureTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        val embed = Embed {
            title = translation.noticeOfDeparture.embedTemplateTitle
            description = translation.noticeOfDeparture.embedTemplateBody
        }

        event.channel.send("", listOf(embed)).setActionRow(
            Button.success("file_notice_of_departure", translation.buttons.textNoticeOfDepartureFile).withEmoji(Emoji.fromFormatted("⏰"))
        ).queue()

        event.reply("Created notice of departure template").setEphemeral(true).queue()
    }
}