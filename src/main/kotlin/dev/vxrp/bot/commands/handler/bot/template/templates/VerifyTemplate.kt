package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.verify.VerifyMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class VerifyTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        VerifyMessageHandler(event.jda, config, translation).sendTemplate(event.channel.asTextChannel(), event.guild!!)

        event.reply_(ColorTool().parse("%filler<1>%")).queue {
            it.deleteOriginal().queue()
        }
    }
}