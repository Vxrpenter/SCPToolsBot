package dev.vxrp.bot.commands.handler.bot.application

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.application.ApplicationMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class ApplicationCommand(val config: Config, val translation: Translation) {
    fun sendActivationMessage(event: SlashCommandInteractionEvent) {
        val valuePair = ApplicationMessageHandler(config, translation).getActivationMenu(event.user.id)
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