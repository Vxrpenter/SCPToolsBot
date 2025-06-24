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

package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.events.entitySelectMenus.TicketEntitySelectMenus
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EntitySelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<EntitySelectInteractionEvent> { event ->
            val launchOptionManager = LaunchOptionManager(config, translation)

            if (launchOptionManager.checkSectionOption(LaunchOptionType.ENTITY_SELECT_LISTENER, LaunchOptionSectionType.TICKET_ENTITY_SELECT_MENUS).engage && event.selectMenu.id!!.startsWith("ticket")) {
                PermissionManager(config, translation).checkStatus(StatusMessageType.PANEL, config.ticket.settings.ticketLogChannel != "")?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: TicketEntitySelectMenus(event, config, translation)
            }
        }
    }
}