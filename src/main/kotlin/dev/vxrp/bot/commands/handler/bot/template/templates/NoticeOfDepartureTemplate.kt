package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.noticeofdeparture.handler.NoticeOfDepartureMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class NoticeOfDepartureTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        NoticeOfDepartureMessageHandler(event.jda, config, translation).sendTemplate(event.channel.asTextChannel())

        event.reply("Created notice of departure template").setEphemeral(true).queue()
    }
}