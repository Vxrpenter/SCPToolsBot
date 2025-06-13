package dev.vxrp.bot.commands.handler.status.playerlist

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.util.statusMappedBots
import dev.vxrp.util.statusMappedServers
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.MessageEmbed
import java.time.Instant

class PlayerlistMessageHandler {
    fun getEmbed(botId: String, translation: Translation): MessageEmbed {
        val builder = StringBuilder()
        val currentPort = statusMappedBots[botId]
        val list = statusMappedServers[currentPort]?.playerList

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
        if (statusMappedServers[currentPort]?.online != null) {
            if (statusMappedServers[currentPort]?.online!!) embedColor = 0x2ECC70
        }

        return Embed {
            color = embedColor
            title = ColorTool().useCustomColorCodes(translation.status.embedPlayerlistTitle).trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedPlayerlistBody
                    .replace("%players%", builder.toString())
                    .replace("%version%", statusMappedServers[currentPort]?.version ?: "&red&&bold&Not Fetched")
                    .replace("%player_number%", statusMappedServers[currentPort]?.players?.split("/".toRegex())?.get(0) ?: 0.toString())
                    .replace("%ff%", statusMappedServers[currentPort]?.ff?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%wl%", statusMappedServers[currentPort]?.wl?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%modded%", statusMappedServers[currentPort]?.modded?.toString() ?: "&red&&bold&Not Fetched")
                    .replace("%mods%", statusMappedServers[currentPort]?.mods?.toString() ?: "&red&&bold&Not Fetched")
                    //Styling for true and false
                    .replace("true", "&green&&bold&true")
                    .replace("false", "&red&&bold&false")
            )
            timestamp = Instant.now()
        }
    }
}