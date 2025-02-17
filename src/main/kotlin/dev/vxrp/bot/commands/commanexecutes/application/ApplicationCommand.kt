package dev.vxrp.bot.commands.commanexecutes.application

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.application.ApplicationMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class ApplicationCommand(val config: Config, val translation: Translation) {
    fun sendActivationMessage(event: SlashCommandInteractionEvent) {
        val valuePair = ApplicationMessageHandler(config, translation).getActivationMenu(event.user.id, event.channel.asTextChannel())
        event.reply_("", listOf(valuePair.first)).addActionRow(
            valuePair.second
        ).queue()
    }

    fun sendDeactivationMessage(event: SlashCommandInteractionEvent) {
        val valuePair = ApplicationMessageHandler(config, translation).getDeactivationMenu()
        event.reply_("", listOf(valuePair.first)).addActionRow(
            valuePair.second
        ).queue()
    }
}