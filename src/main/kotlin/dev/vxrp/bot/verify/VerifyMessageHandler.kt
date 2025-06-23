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

package dev.vxrp.bot.verify

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.slf4j.LoggerFactory

class VerifyMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(VerifyMessageHandler::class.java)

    fun sendTemplate(channel: TextChannel, guild: Guild) {
        val embed = Embed {
            thumbnail = guild.iconUrl
            title = ColorTool().parse(translation.verify.embedTemplateTitle)
            description = ColorTool().parse(translation.verify.embedTemplateBody)
        }

        channel.send("", listOf(embed)).setActionRow(
            Button.link(config.settings.verify.oauthLink, translation.buttons.textVerifyVerify),
            Button.secondary("verify_show_data", translation.buttons.textVerifyShowData),
            Button.danger("verify_delete", translation.buttons.textVerifyDelete)
        ).queue()
    }

    fun sendVerificationMessage(user: User) {
        val embed = Embed {
            color = 0x2ECC70
            thumbnail = user.avatarUrl
            title = ColorTool().parse(translation.verify.embedLogVerifiedTitle
                .replace("%name%", user.globalName.toString()))
            description = ColorTool().parse(translation.verify.embedLogVerifiedBody)
        }

        sendMessage(embed)
    }

    fun sendDeletionMessage(user: User) {
        val embed = Embed {
            color = 0xE74D3C
            thumbnail = user.avatarUrl
            title = ColorTool().parse(translation.verify.embedLogDeletedTitle
                .replace("%name%", user.globalName.toString()))
            description = ColorTool().parse(translation.verify.embedLogDeletedBody)
        }

        sendMessage(embed)
    }

    private fun sendMessage(embed: MessageEmbed) {
        val channel = api.getTextChannelById(config.settings.verify.verifyLogChannel)
        channel?.send("", listOf(embed))?.queue() ?: run {
            logger.error("Could not correctly retrieve verify log channel, does it exist?")
            return
        }
    }
}