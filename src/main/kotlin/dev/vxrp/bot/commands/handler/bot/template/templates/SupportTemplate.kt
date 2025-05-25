package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.handler.TicketMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class SupportTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        TicketMessageHandler(event.jda, config, translation).sendTemplate(event.channel.asTextChannel(), event.guild!!)
        
        event.reply("Created support template").setEphemeral(true).queue()
    }
}