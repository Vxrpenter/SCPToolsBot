package dev.vxrp.bot.commands.listeners

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.commanexecutes.application.ApplicationCommand
import dev.vxrp.bot.commands.commanexecutes.help.HelpCommand
import dev.vxrp.bot.commands.commanexecutes.player.PlayerCommand
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

            "commands.application.default" -> {
                applicationCommand(event)
                return true
            }
        }

        return false
    }

    private fun checkSubInheritance(inherit: String, event: SlashCommandInteractionEvent): Boolean {
        when (inherit) {
            "commands.kill.sub" -> {
                // Test Tes Test
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
        val playerCommand = PlayerCommand(config, translation)

        when (event.getOption("type")?.asString) {
            "statistics"  -> playerCommand.pastePlayerInformation(event)

            "moderation" -> playerCommand.pasteModerationMenu(event)

            "appeals" -> playerCommand.pasteAppealsMenu(event)

            "ticket" -> playerCommand.pasteTicketManagementMenu(event)

            null -> playerCommand.pastePlayerInformation(event)
        }
    }

    private fun settingsCommand(event: SlashCommandInteractionEvent) {
        SettingsCommand(config, translation).pasteSettingsMenu(event)
    }

    private fun applicationCommand(event: SlashCommandInteractionEvent) {
        val option = event.getOption("state")?.asString
        val applicationCommand = ApplicationCommand(config, translation)

        when (option) {
            "active" -> applicationCommand.sendActivationMessage(event)
            "deactivated" -> applicationCommand.sendDeactivationMessage(event)
        }
    }
}