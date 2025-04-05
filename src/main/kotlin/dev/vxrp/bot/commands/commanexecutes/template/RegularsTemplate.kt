package dev.vxrp.bot.commands.commanexecutes.template

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.regulars.RegularsMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class RegularsTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        if (!config.settings.cedmod.active || !config.settings.webserver.active) {
            val embed = Embed {

            }

            return
        }

        RegularsMessageHandler(event.jda, config, translation).sendRegulars(event.channel.asTextChannel())

        event.reply("Created regulars template").setEphemeral(true).queue()
    }
}