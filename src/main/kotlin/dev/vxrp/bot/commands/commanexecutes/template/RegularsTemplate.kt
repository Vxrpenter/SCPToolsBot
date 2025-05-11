package dev.vxrp.bot.commands.commanexecutes.template

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.handler.RegularsMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        if (!config.settings.cedmod.active && !config.settings.xp.active || !config.settings.webserver.active) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendTemplateTitle)
                description = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendTemplateBody)
            }

            event.reply_("", listOf(embed)).setEphemeral(true).queue()
            return
        }

        RegularsMessageHandler(event.jda, config, translation).sendRegulars(event.channel.asTextChannel())

        event.reply("Created regulars template").setEphemeral(true).queue()
    }
}