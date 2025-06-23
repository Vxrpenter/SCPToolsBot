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

package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.GlobalTemplateModals
import dev.vxrp.bot.modals.TicketTemplateModals
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.bot.ticket.handler.TicketSettingsHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import java.time.Instant

class TicketButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        val api = event.jda

        if (event.button.id?.startsWith("ticket_anonymous_accept") == true) {
            val userId = event.button.id!!.split(":")[1]

            event.replyModal(TicketTemplateModals(translation).supportComplaintModal(userId, true)).queue()
        }

        if (event.button.id?.startsWith("ticket_anonymous_deny") == true) {
            val userId = event.button.id!!.split(":")[1]

            event.replyModal(TicketTemplateModals(translation).supportComplaintModal(userId, false)).queue()
        }

        if (event.button.id?.startsWith("ticket_claim") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.support.embedLogClaimedTitle)
                description = ColorTool().parse(translation.support.embedLogClaimedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).claimTicket(event.user, event.channel.asThreadChannel() ,event.channelId!!, event.user.id)
        }

        if (event.button.id?.startsWith("ticket_close") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            event.replyModal(GlobalTemplateModals(translation).reasonModal("ticket_close:${event.channelId}")).queue()
        }

        if (event.button.id?.startsWith("ticket_settings") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return
            if (TicketTable().determineHandler(event.channelId!!)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val ticketHandler = TicketSettingsHandler(api, config, translation)

            val settings = Embed {
                title = ColorTool().parse(translation.support.embedSettingsTitle
                    .replace("%user%", event.user.globalName!!))
                description = ColorTool().parse(translation.support.embedSettingsBody)
                timestamp = Instant.now()
            }

            event.reply_("", listOf(settings)).addActionRow(
                ticketHandler.settingsActionRow(TicketTable().getTicketStatus(event.channelId!!)!!)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("ticket_setting_open") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return
            if (TicketTable().determineHandler(event.channelId!!)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.support.embedLogOpenedTitle)
                description = ColorTool().parse(translation.support.embedLogOpenedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).openTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_pause") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return
            if (TicketTable().determineHandler(event.channelId!!)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val embed = Embed {
                color = 0xf1c40f
                title = ColorTool().parse(translation.support.embedLogPausedTitle)
                description = ColorTool().parse(translation.support.embedLogPausedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).pauseTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_suspend") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return
            if (TicketTable().determineHandler(event.channelId!!)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().parse(translation.support.embedLogSuspendedTitle)
                description = ColorTool().parse(translation.support.embedLogSuspendedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            event.message.delete().queue()
            TicketSettingsHandler(api, config, translation).suspendTicket(event.user, event.channel.asThreadChannel(), event.channelId!!)
        }

        if (event.button.id?.startsWith("ticket_setting_close") == true) {
            if(permissionCheck(PermissionType.TICKET, TicketTable().determineTicketType(event.channelId!!))) return

            event.replyModal(GlobalTemplateModals(translation).reasonModal("ticket_close:${event.channelId}")).queue()
        }

        if (event.button.id?.startsWith("ticket_log_claim") == true) {
            val channelId = event.button.id!!.split(":")[1]
            if(permissionCheck(PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.button.id!!.split(":")[1]))) return

            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.support.embedLogClaimedTitle)
                description = ColorTool().parse(translation.support.embedLogClaimedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).claimTicket(event.user, channel, channelId, event.user.id)
        }

        if (event.button.id?.startsWith("ticket_log_open") == true) {
            val channelId = event.button.id!!.split(":")[1]
            if(permissionCheck(PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.button.id!!.split(":")[1]))) return
            if (TicketTable().determineHandler(channelId)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.support.embedLogOpenedTitle)
                description = ColorTool().parse(translation.support.embedLogOpenedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).openTicket(event.user, channel, channelId)
        }

        if (event.button.id?.startsWith("ticket_log_pause") == true) {
            val channelId = event.button.id!!.split(":")[1]
            if(permissionCheck(PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.button.id!!.split(":")[1]))) return
            if (TicketTable().determineHandler(channelId)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                color = 0xf1c40f
                title = ColorTool().parse(translation.support.embedLogPausedTitle)
                description = ColorTool().parse(translation.support.embedLogPausedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).pauseTicket(event.user, channel, channelId)
        }

        if (event.button.id?.startsWith("ticket_log_suspend") == true) {
            val channelId = event.button.id!!.split(":")[1]
            if(permissionCheck(PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.button.id!!.split(":")[1]))) return
            if (TicketTable().determineHandler(channelId)) {
                event.reply_("", listOf(noHandlerEmbed)).setEphemeral(true).queue()
                return
            }

            val channel = event.jda.getThreadChannelById(channelId)!!
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().parse(translation.support.embedLogSuspendedTitle)
                description = ColorTool().parse(translation.support.embedLogSuspendedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            TicketSettingsHandler(api, config, translation).suspendTicket(event.user, channel, channelId)
        }

        if (event.button.id?.startsWith("ticket_log_close") == true) {
            val channelId = event.button.id!!.split(":")[1]
            if(permissionCheck(PermissionType.TICKET_LOGS, TicketTable().determineTicketType(event.button.id!!.split(":")[1]))) return

            event.replyModal(GlobalTemplateModals(translation).reasonModal("ticket_close:$channelId")).queue()
        }
    }

    private suspend fun permissionCheck(permissionType: PermissionType, ticketType: TicketType): Boolean {
        val permissionPair = PermissionManager(config, translation).determinePermissions(event.user, permissionType, ticketType)
        if (permissionPair.second != null) {
            event.reply_("", listOf(permissionPair.second!!)).setEphemeral(true).queue()
        }
        if (!permissionPair.first) return true
        return false
    }

    private val noHandlerEmbed = Embed {
        title = ColorTool().parse(translation.support.embedTicketNoHandlerTitle)
        description = ColorTool().parse(translation.support.embedTicketNoHandlerBody)
    }
}