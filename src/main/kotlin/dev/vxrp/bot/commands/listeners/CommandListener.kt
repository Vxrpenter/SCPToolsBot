/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.commands.listeners

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.commands.handler.bot.application.ApplicationCommand
import dev.vxrp.bot.commands.handler.bot.help.HelpCommand
import dev.vxrp.bot.commands.handler.bot.noticeofdeparture.NoticeOfDepartureCommand
import dev.vxrp.bot.commands.handler.bot.regulars.RegularsCommand
import dev.vxrp.bot.commands.handler.bot.settings.SettingsCommand
import dev.vxrp.bot.commands.handler.bot.template.TemplateCommandHandler
import dev.vxrp.bot.commands.handler.bot.verify.VerifyCommand
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            if (event.channel.type == ChannelType.PRIVATE) {
                val embed = Embed {
                    color = 0xE74D3C
                    title = ColorTool().parse(translation.permissions.embedCommandDeniedTitle)
                    description = ColorTool().parse(translation.permissions.embedCommandDeniedBody)
                }
                event.reply_("", listOf(embed)).setEphemeral(true).await()
                return@listener
            }
            val commandList = config.extra.commands.commands

            for (command in commandList) {
                if (!event.fullCommandName.contains(command.name)) continue

                checkInheritance(command.inherit, event)

                if (command.subcommands == null) continue
                for (subCommand in command.subcommands) {
                    if (subCommand.name != event.fullCommandName.split(" ".toRegex())[1]) continue

                    if (checkSubInheritance(subCommand.inherit, event)) continue
                }
            }
        }
    }

    private fun checkInheritance(inherit: String, event: SlashCommandInteractionEvent) {
        val launchOptionManager = LaunchOptionManager(config, translation)

        when (inherit) {
            "commands.help.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.HELP_COMMAND).engage) {
                    helpCommand(event)
                }
                return
            }

            "commands.template.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.TEMPLATE_COMMAND).engage) {
                    templateCommand(event)
                }
                return
            }

            "commands.verify.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.VERIFY_COMMAND).engage) {
                    PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                        event.reply_("", listOf(embed)).setEphemeral(true).queue()
                    } ?: verifyCommand(event)
                }
                return
            }

            "commands.settings.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.SETTINGS_COMMAND).engage) {
                    settingsCommand(event)
                }
                return
            }

            "commands.application.default" -> {
                if (launchOptionManager.checkSectionOption(LaunchOptionType.COMMAND_LISTENER, LaunchOptionSectionType.APPLICATION_COMMAND).engage) {
                    PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.ticket.settings.applicationMessageChannel != "")?.let { embed ->
                        event.reply_("", listOf(embed)).setEphemeral(true).queue()
                    } ?: applicationCommand(event)
                }
                return
            }
        }

        return
    }

    private fun checkSubInheritance(inherit: String, event: SlashCommandInteractionEvent): Boolean {
        when (inherit) {
            "notice_of_departure.view.sub" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.noticeOfDeparture.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: NoticeOfDepartureCommand(config, translation).view(event)
                return true
            }

            "notice_of_departure.revoke.sub" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.noticeOfDeparture.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: NoticeOfDepartureCommand(config, translation).revoke(event)
                return true
            }

            "regulars.view.sub" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: RegularsCommand(config, translation).view(event)
                return true
            }

            "regulars.remove.sub" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.COMMAND, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: RegularsCommand(config, translation).remove(event)
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