package dev.vxrp.bot.commands.handler.bot.regulars

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.regulars.handler.RegularsMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsCommand(val config: Config, val translation: Translation) {
    fun view(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (!checkExistence(event, user)) return

        val embed = RegularsMessageHandler(event.jda, config, translation).getSettings(event.user, translation.regulars.embedSettingsViewTitle, translation.regulars.embedSettingsViewBody)
        event.reply_("", listOf(embed)).setEphemeral(true).queue()
    }

    fun remove(event: SlashCommandInteractionEvent) {
        val user = event.options[0].asUser
        if (!checkExistence(event, user)) return

        val embed = Embed {
            color = 0x2ECC70
            title = ColorTool().parse(translation.regulars.embedSyncRemovedTitle)
            description = ColorTool().parse(translation.regulars.embedSyncRemovedBody)
        }

        RegularsManager(event.jda, config, translation).removeSync(user.id)
        event.reply_("", listOf(embed)).queue()
    }

    private fun checkExistence(event: SlashCommandInteractionEvent, user: User): Boolean {
        if (user.isBot || !RegularsTable().exists(user.id)) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().parse(translation.permissions.embedNotFoundTitle)
                description = ColorTool().parse(translation.permissions.embedNotFoundBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            return false
        }
        return true
    }
}