package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.RegularsFileHandler
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.regulars.RegularsMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.UserTable
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

        if (event.button.id?.startsWith("regulars_sync") == true) {
            if (notVerified()) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncGroupSelectTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncGroupSelectBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("regulars_group_select").also {
                    for (group in RegularsFileHandler(config, translation).query()) {
                        it.addOption(group.manifest.name, group.manifest.name)
                    }
                }.build()
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_reactivate") == true) {
            if (notVerified()) return

            RegularsManager(event.jda, config, translation).reactivateSync(event.user.id)
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncReactivatedTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncReactivatedBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_deactivate") == true) {
            if (notVerified()) return

            RegularsManager(event.jda, config, translation).deactivateSync(event.user.id)
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncDeactivatedTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncDeactivatedBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_remove") == true) {
            if (notVerified()) return

            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncRemovedConfirmTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncRemovedConfirmBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                Button.success("regulars_sync_remove_confirm", translation.buttons.textRegularSyncRemove)
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("regulars_sync_remove_confirm") == true) {
            if (notVerified()) return

            RegularsManager(event.jda, config, translation).removeSync(event.user.id)
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncRemovedTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncRemovedBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }

    private fun notVerified(): Boolean {
        if (!UserTable().exists(event.user.id)) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.regulars.embedNotVerifiedTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedNotVerifiedBody)
            }
            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            return true
        }

        return false
    }
}