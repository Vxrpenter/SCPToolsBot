package dev.vxrp.bot.commands.commanexecutes.status

import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class PlayerlistCommand(val config: Config, val translation: Translation, private val statusConstructor: StatusConstructor) {

    fun pastePlayerList(event: SlashCommandInteractionEvent) {
        val embed = Playerlist().getEmbed(event.jda.selfUser.id, translation, statusConstructor)
        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}