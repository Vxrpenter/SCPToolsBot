package dev.vxrp.bot.commands.commanexecutes.player

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.time.Instant

class PlayerCommand(val config: Config, val translation: Translation) {
    fun pastePlayerInformation(event: SlashCommandInteractionEvent) {
        val user = event.getOption("user")!!.asUser
        if (user.isBot) {
            event.reply_("You can only query users").queue()
        }
        val steamId = "test"

        val embed= Embed {
            author {
                name = user.globalName
                iconUrl = user.avatarUrl
                url = "${config.cedmod.instance}/Moderation/PlayerManagement/${steamId}@steam"
            }
            timestamp = Instant.now()
            description  = """
                **──────────────────────────────────────**
                ### `➥` Discord  ID: `%discordId%`
                ### `➥` Timeouted?  ID: `%discordTimeout%`
                ### `➥` Muted?  ID: `%discordMute%`
                
                **─────────────────────────────────────────────────────────────**
            """.trimIndent()
            field {
                name = "`🆔` SteamId"
                value = "➥ %steamId%"
            }
            field {
                name = "`⏰` Playtime"
                value = "➥ %playtime%"
            }
            field {
                name = "`🔨` Currently Banned"
                value = "➥ %banState%"
            }
            field {
                inline = false
                name = "**─────────────────────────────────────────────────────────────**"
            }
            field {
                name = "`⚠` Warns"
                value = "➥ %warns%"
            }
            field {
                name = "`🔇` Mutes"
                value = "➥ %slMutes%"
            }
            field {
                name = "`👁️` Watchlist"
                value = "➥ %watchlist%"
            }
            field {
                name = "**─────────────────────────────────────────────────────────────**"
                value = "This Information is subject to change when panel information changes"
            }
        }

        event.reply_("", listOf(embed)).setEphemeral(true)
            .addActionRow(
                Button.primary("player::stats", "Statistics"),
                Button.primary("player::moderation", "Moderation"),
                Button.primary("player::appeal", "Appeals"),
                Button.primary("player::ticket", "Ticket"),
                Button.link("${config.cedmod.instance}/Moderation/PlayerManagement/${steamId}@steam", "Open on Panel")
            ).queue()
    }

    fun pasteModerationMenu(event: SlashCommandInteractionEvent) {

    }

    fun pasteAppealsMenu(event: SlashCommandInteractionEvent) {

    }

    fun pasteTicketManagementMenu(event: SlashCommandInteractionEvent) {

    }
}