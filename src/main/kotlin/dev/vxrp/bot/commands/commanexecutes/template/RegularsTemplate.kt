package dev.vxrp.bot.commands.commanexecutes.template

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.regulars.RegularsMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        if (!config.settings.cedmod.active || !config.settings.webserver.active) {
            val embed = Embed {
                color = 0xE74D3C
                title = "Could not create Template"
                description = "This template is deactivated as long as you have cedmod and webserver deactivated. Navigate to the config to activate and configure them."
            }

            event.reply_("", listOf(embed)).queue()
            return
        }

        RegularsMessageHandler(event.jda, config, translation).sendRegulars(event.channel.asTextChannel())

        event.reply("Created regulars template").setEphemeral(true).queue()
    }
}