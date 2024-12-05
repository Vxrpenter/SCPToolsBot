package dev.vxrp.bot.commands.commanexecutes.status

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.MessageEmbed
import java.time.Instant

class Playerlist {
    fun getEmbed(botId: String, translation: Translation, statusConst: StatusConst): MessageEmbed {
        val builder = StringBuilder()
        val currentPort = statusConst.mappedBots[botId]
        val list = statusConst.mappedServers[currentPort]?.playerList

        if (list != null) {
            if (list.isEmpty()) builder.append(translation.status.embedPlayerlistEmpty)
            for (player in list) {

                builder.append(
                    ColorTool().useCustomColorCodes(
                        translation.status.embedPlayerlistPlayer
                            .replace("%nickname%", player.nickname.toString())
                            .replace("%id%", player.id.toString())
                    )
                )
            }
        } else {
            builder.append(ColorTool().useCustomColorCodes(translation.status.embedPlayerlistCouldntFetch).trimIndent())
        }

        return Embed {
            title = ColorTool().useCustomColorCodes(translation.status.embedPlayerlistTitle).trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedPlayerlistBody
                    .replace("%players%", builder.toString())
                    .replace("%version%", statusConst.mappedServers[currentPort]?.version ?: "&red&&bold&Not Fetched")
                    .replace(
                        "%player_number%",
                        statusConst.mappedServers[currentPort]?.players?.split("/".toRegex())?.get(0) ?: 0.toString()
                    )
                    .replace("%ff%", statusConst.mappedServers[currentPort]?.ff?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%wl%", statusConst.mappedServers[currentPort]?.wl?.toString() ?: "&red&&bold&Not Fetched")
                    .replace(
                        "%modded%",
                        statusConst.mappedServers[currentPort]?.modded?.toString() ?: "&red&&bold&Not Fetched"
                    )
                    .replace(
                        "%mods%",
                        statusConst.mappedServers[currentPort]?.mods?.toString() ?: "&red&&bold&Not Fetched"
                    )
                    //Styling for true and false
                    .replace("true", "&green&&bold&true")
                    .replace("false", "&red&&bold&false")
            )
            timestamp = Instant.now()
        }
    }
}