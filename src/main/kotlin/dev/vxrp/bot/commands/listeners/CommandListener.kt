package dev.vxrp.bot.commands.listeners

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.commanexecutes.help.HelpCommand
import dev.vxrp.bot.commands.commanexecutes.settings.SettingsCommand
import dev.vxrp.bot.commands.commanexecutes.template.TemplateCommand
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            val commandManager = CommandManager(config, "configs/commands.json")

            val commandList = commandManager.query().commands

            for (command in commandList) {
                if (!event.fullCommandName.contains(command.name)) continue

                if (checkInheritance(command.inherit, event)) break

                for (subCommand in command.subcommands!!) {
                    if (subCommand.name != event.fullCommandName.split(" ".toRegex())[1]) continue

                    if (checkSubInheritance(subCommand.inherit, event)) continue
                }
            }
        }
    }

    private fun checkInheritance(inherit: String, event: SlashCommandInteractionEvent): Boolean {
        when (inherit) {
            "commands.help.default" -> {
                helpCommand(event)
                return true
            }

            "commands.template.default" -> {
                templateCommand(event)
                return true
            }

            "commands.notice_of_departure.default" -> {
                noticeOfDepartureCommand(event)
                return true
            }

            "commands.regulars.default" -> {
                regularsCommand(event)
                return true
            }

            "commands.player.default" -> {
                playerCommand(event)
                return true
            }

            "commands.settings.default" -> {
                settingsCommand(event)
                return true
            }
        }

        return false
    }

    private fun checkSubInheritance(inherit: String, event: SlashCommandInteractionEvent): Boolean {
        when (inherit) {
            "commands.kill.sub" -> {
                // Test Tes Te
                return true
            }
        }

        return false
    }

    private fun helpCommand(event: SlashCommandInteractionEvent) {
        HelpCommand(translation).pasteHelpMenu(event)
    }

    private fun templateCommand(event: SlashCommandInteractionEvent) {
        TemplateCommand(config, translation).findOption(event)
    }

    private fun noticeOfDepartureCommand(event: SlashCommandInteractionEvent) {

    }

    private fun regularsCommand(event: SlashCommandInteractionEvent) {

    }

    private fun playerCommand(event: SlashCommandInteractionEvent) {

    }

    private fun settingsCommand(event: SlashCommandInteractionEvent) {
        SettingsCommand(config, translation).pasteSettingsMenu(event)
    }
}