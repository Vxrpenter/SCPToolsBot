/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.commands.handler.bot.settings

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.upstreamVersion
import io.github.vxrpenter.cedmod.Cedmod
import io.github.vxrpenter.cedmod.exceptions.CallFailureException
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
        var cedmod = translation.settings.textCedmodOffline
        val currentColor = 0x00DC82
        if (checkCedmod()) cedmod = translation.settings.textCedmodOnline

        val embed = Embed {
            author {
                name = event.guild?.name
                iconUrl = event.guild?.iconUrl
            }
            color = currentColor
            title = ColorTool().parse(translation.settings.embedSettingsTitle.trimIndent())
            timestamp = Instant.now()
            description = ColorTool().parse(translation.settings.embedSettingsBody.trimIndent())

            field {
                inline = true
                name =
                    ColorTool().parse(translation.settings.embedSettingsFieldLanguageTitle.trimIndent())
                value = ColorTool().parse(
                    translation.settings.embedSettingsFieldLanguageValue
                        .replace("%language%", config.settings.loadTranslation).trimIndent()
                )
            }

            field {
                inline = true
                name = ColorTool().parse(translation.settings.embedSettingsFieldGuildTitle.trimIndent())
                value = ColorTool().parse(translation.settings.embedSettingsFieldGuildValue
                    .replace("%isAvailable%", "${event.guild?.idLong?.let { event.jda.isUnavailable(it) }}")
                    .trimIndent()
                )
            }

            field {
                inline = true
                name =
                    ColorTool().parse(translation.settings.embedSettingsFieldDatabaseTitle.trimIndent())
                value = ColorTool().parse(
                    translation.settings.embedSettingsFieldDatabaseValue
                        .replace("%state%", translation.settings.textDatabaseOnline).trimIndent()
                )
            }

            field {
                inline = true
                name = ColorTool().parse(translation.settings.embedSettingsFieldCedmodTitle.trimIndent())
                value = cedmod
            }

            field {
                inline = true
                name = ColorTool().parse(translation.settings.embedSettingsFieldVersionTitle.trimIndent())
                value = ColorTool().parse(
                    translation.settings.embedSettingsFieldVersionValue
                        .replace("%version%", version()).trimIndent()
                )
            }

            field {
                inline = true
                name = ColorTool().parse(translation.settings.embedSettingsFieldBuildTitle.trimIndent())
                value = ColorTool().parse(
                    translation.settings.embedSettingsFieldBuildValue
                        .replace(
                            "%build%",
                            upstreamVersion!!
                        ).trimIndent()
                )
            }

            field {
                inline = true
                name = ColorTool().parse(translation.settings.embedSettingsFieldGatewayTitle.trimIndent())
                value = ColorTool().parse(
                    translation.settings.embedSettingsFieldGatewayValue
                        .replace("%time%", "${event.jda.gatewayPing}").trimIndent()
                )
            }

            field {
                inline = true
                name = "\u200E \n\u200E"
                value = "\u200E"
            }

            field {
                inline = true
                name = ColorTool().parse(translation.settings.embedSettingsFieldRestTitle.trimIndent())
                value = ColorTool().parse(
                    translation.settings.embedSettingsFieldRestValue
                        .replace("%time%", "${event.jda.restPing.complete()}").trimIndent()
                )
            }
        }

        event.reply_("", listOf(embed)).setActionRow(
            Button.secondary("start_page", ColorTool().parse(translation.buttons.textSettingsStart))
                .asDisabled(),
            Button.primary(
                "configure_page",
                ColorTool().parse(translation.buttons.textSettingsConfigure)
            ),
            Button.primary(
                "current_settings",
                ColorTool().parse(translation.buttons.textSettingsCurrent)
            ),
            Button.primary("bot_info", ColorTool().parse(translation.buttons.textSettingsInformation)),
            Button.link(
                "https://github.com/Vxrpenter/SCPToolsBot",
                ColorTool().parse(translation.buttons.textSettingsNews)
            ).withEmoji(Emoji.fromFormatted("🗞️"))
        ).setEphemeral(true).queue()
    }

    private fun checkCedmod(): Boolean {
        if (config.settings.cedmod.active) {
            try {
                Cedmod(config.settings.cedmod.instance, config.settings.cedmod.api).changelogGet()
                return true
            } catch (_: UnknownHostException) {
                return false
            } catch (_: CallFailureException) {
                return false
            } catch (_: IllegalArgumentException) {
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