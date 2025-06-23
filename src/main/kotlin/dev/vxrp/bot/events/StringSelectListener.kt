/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.events.stringSelectMenus.ApplicationStringSelectMenus
import dev.vxrp.bot.events.stringSelectMenus.RegularsStringSelectMenus
import dev.vxrp.bot.events.stringSelectMenus.TicketStringSelectMenus
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class StringSelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<StringSelectInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.STRING_SELECT_LISTENER, LaunchOptionSectionType.TICKET_STRING_SELECT_MENUS).engage && event.selectMenu.id!!.startsWith("ticket")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.ticketLogChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: TicketStringSelectMenus(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.STRING_SELECT_LISTENER, LaunchOptionSectionType.APPLICATION_STRING_SELECT_MENUS).engage && event.selectMenu.id!!.startsWith("application")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.applicationMessageChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: ApplicationStringSelectMenus(event, config, translation).init()
            }

            if (launchOptionManager.checkSectionOption(LaunchOptionType.STRING_SELECT_LISTENER, LaunchOptionSectionType.REGULARS_STRING_SELECT_MENUS).engage && event.selectMenu.id!!.startsWith("regulars")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: RegularsStringSelectMenus(event, config, translation).init()
            }
        }
    }
}