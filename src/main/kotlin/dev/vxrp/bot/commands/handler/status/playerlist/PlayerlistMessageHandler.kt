package dev.vxrp.bot.commands.handler.status.playerlist

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.MessageEmbed
import java.time.Instant
import kotlin.collections.get

class PlayerlistMessageHandler {
    fun getEmbed(botId: String, translation: Translation, statusConstructor: StatusConstructor): MessageEmbed {
        val builder = StringBuilder()
        val currentPort = statusConstructor.mappedBots[botId]
        val list = statusConstructor.mappedServers[currentPort]?.playerList

        if (list != null) {
            if (list.isEmpty()) builder.append(translation.status.embedPlayerlistEmpty)
            for (player in list) {

                builder.append(
                    ColorTool().useCustomColorCodes(translation.status.embedPlayerlistPlayer
                            .replace("%nickname%", player.nickname.toString())))
            }
        } else {
            builder.append(ColorTool().useCustomColorCodes(translation.status.embedPlayerlistCouldntFetch).trimIndent())
        }

        var embedColor = 0xE74D3C
        if (statusConstructor.mappedServers[currentPort]?.online != null) {
            if (statusConstructor.mappedServers[currentPort]?.online!!) embedColor = 0x2ECC70
        }

        return Embed {
            color = embedColor
            title = ColorTool().useCustomColorCodes(translation.status.embedPlayerlistTitle).trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedPlayerlistBody
                    .replace("%players%", builder.toString())
                    .replace("%version%", statusConstructor.mappedServers[currentPort]?.version ?: "&red&&bold&Not Fetched")
                    .replace("%player_number%", statusConstructor.mappedServers[currentPort]?.players?.split("/".toRegex())?.get(0) ?: 0.toString())
                    .replace("%ff%", statusConstructor.mappedServers[currentPort]?.ff?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%wl%", statusConstructor.mappedServers[currentPort]?.wl?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%modded%", statusConstructor.mappedServers[currentPort]?.modded?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%mods%", statusConstructor.mappedServers[currentPort]?.mods?.toString() ?: "&red&&bold&Not Fetched")
                    //Styling for true and false
                    .replace("true", "&green&&bold&true")
                    .replace("false", "&red&&bold&false")
            )
            timestamp = Instant.now()
        }
    }
}