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
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.regulars.handler.RegularsFileHandler
import dev.vxrp.bot.regulars.handler.RegularsMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.UserTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class RegularsButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        if (event.button.id?.startsWith("regulars_open_settings") == true) {
            val regularsMessageHandler = RegularsMessageHandler(event.jda, config, translation)

            event.reply_("", listOf(regularsMessageHandler.getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.equals("regulars_sync") == true) {
            if (notVerified()) return

            val embed = Embed {
                title = ColorTool().parse(translation.regulars.embedSyncGroupSelectTitle)
                description = ColorTool().parse(translation.regulars.embedSyncGroupSelectBody)
            }

            event.message.delete().queue()
            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("regulars_group_select").also {
                    for (group in RegularsFileHandler(config).query()) {
                        it.addOption(group.manifest.name, group.manifest.name)
                    }
                }.build()
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_reactivate") == true) {
            val regularsMessageHandler = RegularsMessageHandler(event.jda, config, translation)
            if (notVerified()) return

            RegularsManager(event.jda, config, translation).reactivateSync(event.user.id)
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.regulars.embedSyncReactivatedTitle)
                description = ColorTool().parse(translation.regulars.embedSyncReactivatedBody)
            }

            event.message.delete().queue()
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            event.reply_("", listOf(RegularsMessageHandler(event.jda, config, translation).getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_deactivate") == true) {
            val regularsMessageHandler = RegularsMessageHandler(event.jda, config, translation)
            if (notVerified()) return

            RegularsManager(event.jda, config, translation).deactivateSync(event.user.id)
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.regulars.embedSyncDeactivatedTitle)
                description = ColorTool().parse(translation.regulars.embedSyncDeactivatedBody)
            }

            event.message.delete().queue()
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            event.reply_("", listOf(RegularsMessageHandler(event.jda, config, translation).getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.equals("regulars_sync_remove") == true) {
            if (notVerified()) return

            val embed = Embed {
                title = ColorTool().parse(translation.regulars.embedSyncRemovedConfirmTitle)
                description = ColorTool().parse(translation.regulars.embedSyncRemovedConfirmBody)
            }

            event.message.delete().queue()
            event.reply_("", listOf(embed)).addActionRow(
                Button.success("regulars_sync_remove_confirm", translation.buttons.textRegularSyncRemove)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_remove_confirm") == true) {
            val regularsMessageHandler = RegularsMessageHandler(event.jda, config, translation)
            if (notVerified()) return

            RegularsManager(event.jda, config, translation).removeSync(event.user.id)
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().parse(translation.regulars.embedSyncRemovedTitle)
                description = ColorTool().parse(translation.regulars.embedSyncRemovedBody)
            }

            event.message.delete().queue()
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            event.reply_("", listOf(RegularsMessageHandler(event.jda, config, translation).getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }
    }

    private fun notVerified(): Boolean {
        if (!UserTable().exists(event.user.id)) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().parse(translation.regulars.embedNotVerifiedTitle)
                description = ColorTool().parse(translation.regulars.embedNotVerifiedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            return true
        }

        return false
    }
}