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
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncSentTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncSentBody)
            }

            event.message.delete().queue()
            event.hook.send("", listOf(embed)).setEphemeral(true).queue()
            event.reply_("", listOf(RegularsMessageHandler(event.jda, config, translation).getSettings(event.user))).addActionRow(
                regularsMessageHandler.getSettingsActionRow(event.user.id)
            ).setEphemeral(true).queue()
        }
    }
}