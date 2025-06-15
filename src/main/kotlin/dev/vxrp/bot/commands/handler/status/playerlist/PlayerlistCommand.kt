package dev.vxrp.bot.commands.handler.status.playerlist

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class PlayerlistCommand(val config: Config, val translation: Translation) {

    fun pastePlayerList(event: SlashCommandInteractionEvent) {
        val embed = PlayerlistMessageHandler().getEmbed(event.jda.selfUser.id, translation)
        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}