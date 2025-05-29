package dev.vxrp.bot.commands.handler.bot.regulars

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.regulars.handler.RegularsMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsCommand(val config: Config, val translation: Translation) {
    fun view(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (user.isBot) return

        if (RegularsTable().exists(user.id)) {
            val embed = RegularsMessageHandler(event.jda, config, translation).getSettings(event.user, translation.regulars.embedSettingsViewTitle, translation.regulars.embedSettingsViewBody)

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        } else {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.permissions.embedNotFoundTitle)
                description = ColorTool().useCustomColorCodes(translation.permissions.embedNotFoundBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }

    fun remove(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (user.isBot) return

        if (RegularsTable().exists(user.id)) {
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.regulars.embedSyncRemovedTitle)
                description = ColorTool().useCustomColorCodes(translation.regulars.embedSyncRemovedBody)
            }

            RegularsManager(event.jda, config, translation).removeSync(user.id)
            event.reply_("", listOf(embed)).queue()
        } else {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.permissions.embedNotFoundTitle)
                description = ColorTool().useCustomColorCodes(translation.permissions.embedNotFoundBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
        }
    }
}