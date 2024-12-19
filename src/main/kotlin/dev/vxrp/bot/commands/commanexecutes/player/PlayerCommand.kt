package dev.vxrp.bot.commands.commanexecutes.player

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.api.sla.cedmod.Cedmod
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.time.Instant

class PlayerCommand(val config: Config, val translation: Translation) {
    fun pastePlayerInformation(event: SlashCommandInteractionEvent) {
        event.deferReply().setEphemeral(true).queue()
        val user = event.getOption("user")!!.asUser
        if (user.isBot) {
            event.reply_("You can only query users").setEphemeral(true).queue()
            return
        }

        val player = Cedmod(config.cedmod.instance, config.cedmod.api).playerQuery("${user.id}@discord", moderationData = true, activityMin = event.getOption("timeframe")!!.asInt).players[0]

        val steamId = "❌ Not Available"
        val playtime = "${player.activity}"
        val banState = player.additionalData.ban
        val warns = player.additionalData.warnLogs
        val mutes = player.additionalData.mute
        val watchlist = player.additionalData.watchlist

        val embedSuccess= Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
                url = "${config.cedmod.instance}/Moderation/PlayerManagement/${player.userId}"
            }
            color = 0x0
            timestamp = Instant.now()
            description  = ColorTool().useCustomColorCodes(translation.player.embedStatisticsBody
                .replace("%discordId%", user.id)
                .replace("%discordTimeout%", "❌ Not Available")
                .replace("%discordMute%", "❌ Not Available").trimIndent())
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsSteamIdFieldName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsSteamIdFieldValue
                    .replace("%steamId%", steamId).trimIndent())
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsPlaytimeFiledName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsPlaytimeFieldValue
                    .replace("%playtime%", playtime).trimIndent())
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsBannedFieldName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsBannedFieldValue
                    .replace("%banState%", banState).trimIndent())
            }
            field {
                inline = false
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsFillerOneFiledName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsFillerOneFiledValue.trimIndent())
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsWarnsFieldName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsWarnsFieldValue
                    .replace("%warns%", warns).trimIndent())
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsMutesFieldName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsMutesFieldValue
                    .replace("%mutes%", mutes).trimIndent())
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsWatchlistFieldName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsWatchlistFieldValue
                    .replace("%watchlist%", watchlist).trimIndent())
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.player.embedStatisticsFillerTwoFiledName.trimIndent())
                value = ColorTool().useCustomColorCodes(translation.player.embedStatisticsFillerTwoFiledValue.trimIndent())
            }
        }

        event.hook.send("", listOf(embedSuccess)).setEphemeral(true)
            .addActionRow(
                Button.primary("player::stats", "Statistics"),
                Button.primary("player::moderation", "Moderation"),
                Button.primary("player::appeal", "Appeals"),
                Button.primary("player::ticket", "Ticket"),
                Button.link("${config.cedmod.instance}/Moderation/PlayerManagement/${player.userId}", "Open on Panel")
            ).queue()
    }

    fun pasteModerationMenu(event: SlashCommandInteractionEvent) {

    }

    fun pasteAppealsMenu(event: SlashCommandInteractionEvent) {

    }

    fun pasteTicketManagementMenu(event: SlashCommandInteractionEvent) {

    }
}