package dev.vxrp.bot.commands.commanexecutes.status

import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class PlayerlistCommand(val config: Config, val translation: Translation, private val statusConstructor: StatusConstructor) {

    fun pastePlayerList(event: SlashCommandInteractionEvent) {
        val embed = Playerlist().getEmbed(event.jda.selfUser.id, translation, statusConstructor)
        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}