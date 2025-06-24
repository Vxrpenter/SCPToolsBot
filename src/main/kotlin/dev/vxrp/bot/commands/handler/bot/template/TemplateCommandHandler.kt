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

package dev.vxrp.bot.commands.handler.bot.template

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.commands.handler.bot.template.templates.NoticeOfDepartureTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.RegularsTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.SupportTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.VerifyTemplate
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TemplateCommandHandler(val config: Config, private val translation: Translation) {
    fun findOption(event: SlashCommandInteractionEvent) {
        val option = event.getOption("template")?.asString

        when (option) {
            "support" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.ticket.settings.ticketLogChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: SupportTemplate(config, translation).pasteTemplate(event)
            }

            "verify" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: VerifyTemplate(config, translation).pasteTemplate(event)
            }

            "notice_of_departure" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.settings.noticeOfDeparture.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: NoticeOfDepartureTemplate(config, translation).pasteTemplate(event)
            }

            "regulars" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: RegularsTemplate(config, translation).pasteTemplate(event)
            }
        }
    }
}