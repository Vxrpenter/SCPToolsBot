package dev.vxrp.bot.commands.commanexecutes.settings

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.api.github.Github
import dev.vxrp.api.sla.cedmod.Cedmod
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.io.InputStreamReader
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

class SettingsCommand(val config: Config, val translation: Translation) {
    fun pasteSettingsMenu(event: SlashCommandInteractionEvent) {
        var cedmod = ":red_circle: Offline"
        val currentColor = 0x00DC82
        if (checkCedmod()) cedmod = ":green_circle: Online"

        val embed = Embed {
            author {
                name = event.guild?.name
                iconUrl = event.guild?.iconUrl
            }
            color = currentColor
            title = "Settings"
            timestamp = Instant.now()
            description = ColorTool().useCustomColorCodes("""
                In this settings menu you will be able to:
                - change config settings
                - view current settings
                - view bot information
                &filler&
                Click on the buttons below to switch between pages. Information for the main page may take longer to load because a lot of information is queried at the same time
                &filler&
                """.trimIndent())

            field {
                inline = true
                name = "Language"
                value = "`${config.loadTranslation}`"
            }

            field {
                inline = true
                name = " \u200E  \u200E  \u200E  \u200E  \u200E \u200E  \u200E  Guild Unavailable"
                value = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  `${event.guild?.idLong?.let { event.jda.isUnavailable(it) }}`"
            }

            field {
                inline = true
                name = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E Database"
                value = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E :green_circle: Online"
            }

            field {
                inline = true
                name = "Cedmod"
                value = cedmod
            }

            field {
                inline = true
                name = " \u200E  \u200E  \u200E  \u200E  \u200E \u200E  \u200E  Version"
                value = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  `${version()}`"
            }

            field {
                inline = true
                name = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E Latest Build"
                value = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E `${Github().checkForUpdatesByTag("https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags", false).toString()}`"
            }

            field {
                inline = true
                name = "\u200E\nGateway Response Time"
                value = "||Received in ${event.jda.gatewayPing} ms||"
            }

            field {
                inline = true
                name = "\u200E \n\u200E"
                value = "\u200E"
            }

            field {
                inline = true
                name =  "\u200E \n \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  REST Response Time"
                value = " \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E  \u200E \u200E \u200E \u200E  ||Received in ${event.jda.restPing.complete()} ms||"
            }
        }

        event.reply_("", listOf(embed)).setActionRow(
            Button.secondary("start_page", "Start").asDisabled(),
            Button.primary("configure_page", "Configure"),
            Button.primary("current_settings", "Current Settings"),
            Button.primary("bot_info", "Information"),
            Button.link("https://github.com/Vxrpenter/SCPToolsBot", "News").withEmoji(Emoji.fromFormatted("🗞️"))
        ).setEphemeral(true).queue()
    }

    private fun checkCedmod(): Boolean {
        if (config.cedmod.active) {
            try {
                val changelog = Cedmod(config.cedmod.instance, config.cedmod.api).changelogGet()
                return true
            } catch (e: UnknownHostException) {
                return false
            }
        }
        return false
    }

    private fun version(): String {
        val properties = Properties()

        SettingsCommand::class.java.getResourceAsStream("/dev/vxrp/version.properties").use { versionPropertiesStream ->
            checkNotNull(versionPropertiesStream) { "Version properties file does not exist" }
            properties.load(InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8))
        }

        return properties.getProperty("version")
    }
}