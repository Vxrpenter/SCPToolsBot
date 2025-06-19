package dev.vxrp.bot.commands.listeners

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.commands.handler.status.playerlist.PlayerlistCommand
import dev.vxrp.bot.commands.handler.status.status.StatusCommand
import dev.vxrp.bot.commands.handler.status.template.TemplateCommandHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class StatusCommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {

    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            if (event.channel.type == ChannelType.PRIVATE) return@listener
            val commandList = config.extra.commands.statusCommands

            for (command in commandList) {
                if (command.name != event.fullCommandName) continue
                checkInheritance(command.inherit, event)
                break
            }
        }
    }

    private suspend fun checkInheritance(inherit: String, event: SlashCommandInteractionEvent) {
        val launchOptionManager = LaunchOptionManager(config, translation)

        when (inherit) {
            "status_commands.status.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.STATUS_COMMAND_LISTENER, LaunchOptionSectionType.STATUS_COMMAND).engage) statusCommand(event)
            }

            "status_commands.playerlist.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.STATUS_COMMAND_LISTENER, LaunchOptionSectionType.STATUS_PLAYERLIST_COMMAND).engage) playerListCommand(event)
            }

            "status_commands.template.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.STATUS_COMMAND_LISTENER, LaunchOptionSectionType.STATUS_TEMPLATE_COMMAND).engage) templateCommand(event)
            }
        }
    }

    private fun statusCommand(event: SlashCommandInteractionEvent) {
        if (event.getOption("setting")?.asString == "maintenance") {
            StatusCommand(config, translation).changeMaintenanceState(event)
        }
    }

    private fun playerListCommand(event: SlashCommandInteractionEvent) {
        PlayerlistCommand(config, translation).pastePlayerList(event)
    }

    private suspend fun templateCommand(event: SlashCommandInteractionEvent) {
        if (event.getOption("template")?.asString == "playerlist") TemplateCommandHandler(config, translation).pastePlayerList(event)
    }
}