package dev.vxrp.bot.events.stringSelectMenus

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

class RegularsStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        if (event.selectMenu.id?.startsWith("regulars_group_select") == true) {
            RegularsManager(config, translation).syncRegulars(event.user.id, event.selectedOptions[0].value)
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncSentTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncSentBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }
}