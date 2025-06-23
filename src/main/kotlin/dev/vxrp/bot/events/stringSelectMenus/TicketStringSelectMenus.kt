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

package dev.vxrp.bot.events.stringSelectMenus

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.TicketTemplateModals
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class TicketStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        val ticketTemplateModals = TicketTemplateModals(translation)

        if (event.selectedOptions[0].value.startsWith("general")) {
            event.replyModal(ticketTemplateModals.supportGeneralModal()).queue()
        }

        if (event.selectedOptions[0].value.startsWith("report")) {
            val embed = Embed {
                title = ColorTool().parse(translation.support.embedReportUserTitle)
                description = ColorTool().parse(translation.support.embedReportUserBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                EntitySelectMenu.create("ticket_report", EntitySelectMenu.SelectTarget.USER).build())
                .setEphemeral(true).queue()
        }

        if (event.selectedOptions[0].value.startsWith("error")) {
            event.replyModal(ticketTemplateModals.supportErrorModal()).queue()
        }

        if (event.selectedOptions[0].value.startsWith("unban")) {
            event.replyModal(ticketTemplateModals.supportUnbanModal()).queue()
        }

        if (event.selectedOptions[0].value.startsWith("complaint")) {
            val embed = Embed {
                title = ColorTool().parse(translation.support.embedComplaintUserTitle)
                description = ColorTool().parse(translation.support.embedComplaintUserBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                EntitySelectMenu.create("ticket_complaint", EntitySelectMenu.SelectTarget.USER).build())
                .setEphemeral(true).queue()
        }

        if (event.selectedOptions[0].value.startsWith("application")) {
            val embed = Embed {
                title = ColorTool().parse(translation.support.embedApplicationPositionTitle)
                description = ColorTool().parse(translation.support.embedApplicationPositionBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("application_position").also {
                    for(type in config.ticket.applicationTypes) {
                        it.addOption(type.name, type.roleID, type.description, Emoji.fromFormatted(type.emoji))
                    }
                }.build()
            ).setEphemeral(true).queue()
        }
    }
}