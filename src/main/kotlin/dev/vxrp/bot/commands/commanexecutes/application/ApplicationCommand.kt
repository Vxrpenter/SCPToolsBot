package dev.vxrp.bot.commands.commanexecutes.application

import dev.vxrp.bot.application.ApplicationManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class ApplicationCommand(val config: Config, val translation: Translation) {
    fun sendMessage(event: SlashCommandInteractionEvent) {
        ApplicationManager(config, translation).sendActivationMenu(event.user.id, event.channel.asTextChannel())
    }
}