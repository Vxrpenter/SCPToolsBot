package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.vxrp.bot.regulars.handler.RegularsMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        RegularsMessageHandler(event.jda, config, translation).sendRegulars(event.channel.asTextChannel())

        event.reply("Created regulars template").setEphemeral(true).queue()
    }
}