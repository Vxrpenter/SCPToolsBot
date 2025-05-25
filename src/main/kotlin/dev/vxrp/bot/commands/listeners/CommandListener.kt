package dev.vxrp.bot.commands.listeners

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.commands.handler.bot.application.ApplicationCommand
import dev.vxrp.bot.commands.handler.bot.help.HelpCommand
import dev.vxrp.bot.commands.handler.bot.settings.SettingsCommand
import dev.vxrp.bot.commands.handler.bot.template.TemplateCommandHandler
import dev.vxrp.bot.commands.handler.bot.verify.VerifyCommand
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            val commandList = config.extra.commands.commands

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
        val launchOptionManager = LaunchOptionManager(config, translation)

        when (inherit) {
            "commands.help.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.HELP_COMMAND).engage) {
                    helpCommand(event)
                }
                return true
            }

            "commands.template.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.TEMPLATE_COMMAND).engage) {
                    templateCommand(event)
                }
                return true
            }

            "commands.verify.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.VERIFY_COMMAND).engage) {
                    PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                        event.reply_("", listOf(embed)).setEphemeral(true).queue()
                    } ?: verifyCommand(event)
                }
                return true
            }

            "commands.notice_of_departure.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.NOTICE_OF_DEPARTURE_COMMAND).engage) {
                    PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.noticeOfDeparture.active)?.let { embed ->
                        event.reply_("", listOf(embed)).setEphemeral(true).queue()
                    } ?: noticeOfDepartureCommand(event)
                }
                return true
            }

            "commands.regulars.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.REGULARS_COMMAND).engage) {
                    PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                        event.reply_("", listOf(embed)).setEphemeral(true).queue()
                    } ?: regularsCommand(event)
                }
                return true
            }

            "commands.settings.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.SETTINGS_COMMAND).engage) {
                    settingsCommand(event)
                }
                return true
            }

            "commands.application.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.APPLICATION_COMMAND).engage) {
                    PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.ticket.settings.applicationMessageChannel != "")?.let { embed ->
                        event.reply_("", listOf(embed)).setEphemeral(true).queue()
                    } ?: applicationCommand(event)
                }
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
        TemplateCommandHandler(config, translation).findOption(event)
    }

    private fun verifyCommand(event: SlashCommandInteractionEvent) {
        VerifyCommand(config, translation).pasteVerifyMenu(event)
    }

    private fun noticeOfDepartureCommand(event: SlashCommandInteractionEvent) {

    }

    private fun regularsCommand(event: SlashCommandInteractionEvent) {

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