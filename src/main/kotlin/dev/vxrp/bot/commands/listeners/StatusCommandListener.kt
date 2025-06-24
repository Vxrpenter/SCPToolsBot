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
import dev.vxrp.bot.commands.handler.status.playerlist.PlayerlistCommand
import dev.vxrp.bot.commands.handler.status.status.StatusCommand
import dev.vxrp.bot.commands.handler.status.template.TemplateCommandHandler
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

class StatusCommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {

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