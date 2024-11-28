package dev.vxrp.bot.commands.commanexecutes.status

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.time.Instant

class PlayerlistCommands(val config: Config, val translation: Translation, private val statusConst: StatusConst) {

    fun pastePlayerList(event: SlashCommandInteractionEvent) {
        val builder = StringBuilder()
        val currentPort = statusConst.mappedBots[event.jda.selfUser.id]
        val list = statusConst.mappedServers[currentPort]?.playerList

        if (list != null) {
            if (list.isEmpty()) builder.append("&red&The server is empty")
            for (player in list) {
                builder.append("&reset&&bold&${player.nickname} &reset&(&green&&bold&${player.id}&reset&)\n")
            }
        } else {
            builder.append("&red&Could not correctly load playerlist")
        }

        val embed = Embed {
            title = "Current Player List (this is not live updating)"
            description = ColorTool().useCustomColorCodes("""
                ```ansi
                $builder
                ```
            """.trimIndent())
            timestamp = Instant.now()
        }

        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}