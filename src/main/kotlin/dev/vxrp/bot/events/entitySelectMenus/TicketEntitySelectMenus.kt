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

package dev.vxrp.bot.events.entitySelectMenus

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.TicketTemplateModals
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class TicketEntitySelectMenus(val event: EntitySelectInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.selectMenu.id?.startsWith("ticket_report") == true) {
            val user = event.values[0]
            event.replyModal(TicketTemplateModals(translation).supportReportModal(user.id)).queue()
        }

        if (event.selectMenu.id?.startsWith("ticket_complaint") == true) {
            val user = event.values[0]
            val embed = Embed {
                title = ColorTool().parse(translation.support.embedComplaintAnonymousTitle)
                description = ColorTool().parse(translation.support.embedComplaintAnonymousBody)
            }

            event.reply_("", listOf(embed)).setActionRow(
                Button.success("ticket_anonymous_accept:${user.id}", translation.buttons.textSupportAnonymousAccept).withEmoji(
                    Emoji.fromFormatted("🔒")),
                Button.danger("ticket_anonymous_deny:${user.id}", translation.buttons.textSupportAnonymousDeny).withEmoji(Emoji.fromFormatted("🔓"))
            ).setEphemeral(true).queue()
        }
    }
}