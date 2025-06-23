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

package dev.vxrp.bot.events.buttons

import dev.vxrp.bot.commands.handler.bot.help.HelpCommand
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class HelpButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        if (event.button.id?.startsWith("help_first_page") == true) {
            event.deferEdit().queue {
                event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages().first())
                    .setActionRow(HelpCommand(translation).actionRow(0)).queue()
            }
        }

        if (event.button.id?.startsWith("help_last_page") == true) {
            event.deferEdit().queue {
                event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages().last())
                    .setActionRow(HelpCommand(translation).actionRow(5)).queue()
            }
        }

        if (event.button.id?.startsWith("help_go_back") == true) {
            val page =
                event.componentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt() - 1
            event.deferEdit().queue {
                event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages()[page])
                    .setActionRow(HelpCommand(translation).actionRow(page)).queue()
            }
        }

        if (event.button.id?.startsWith("help_go_forward") == true) {
            val page =
                event.componentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt() + 1
            event.deferEdit().queue {
                event.channel.editMessageEmbedsById(event.messageId, HelpCommand(translation).pages()[page])
                    .setActionRow(HelpCommand(translation).actionRow(page)).queue()
            }
        }
    }
}