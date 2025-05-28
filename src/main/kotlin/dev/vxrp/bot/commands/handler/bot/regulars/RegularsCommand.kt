package dev.vxrp.bot.commands.handler.bot.regulars

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsCommand(val config: Config, val translation: Translation) {
    fun view(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser

    }

    fun remove(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser

    }
}