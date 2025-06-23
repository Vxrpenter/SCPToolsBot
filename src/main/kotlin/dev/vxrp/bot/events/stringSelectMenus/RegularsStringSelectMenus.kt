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
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.regulars.handler.RegularsMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

class RegularsStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    suspend fun init() {
        if (event.selectMenu.id?.startsWith("regulars_group_select") == true) {
            val regularsMessageHandler = RegularsMessageHandler(event.jda, config, translation)

            RegularsManager(event.jda, config, translation).syncRegulars(event.user.id, event.selectedOptions[0].value)
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.regulars.embedSyncSentTitle)
                description = ColorTool().parse(translation.regulars.embedSyncSentBody)
            }

            event.message.delete().queue()
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            event.reply_("", listOf(RegularsMessageHandler(event.jda, config, translation).getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }
    }
}