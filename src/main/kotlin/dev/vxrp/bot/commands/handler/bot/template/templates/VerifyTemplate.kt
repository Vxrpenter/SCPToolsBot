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

package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.verify.VerifyMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class VerifyTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        VerifyMessageHandler(event.jda, config, translation).sendTemplate(event.channel.asTextChannel(), event.guild!!)

        event.reply_(ColorTool().parse("%filler<1>%")).queue {
            it.deleteOriginal().queue()
        }
    }
}