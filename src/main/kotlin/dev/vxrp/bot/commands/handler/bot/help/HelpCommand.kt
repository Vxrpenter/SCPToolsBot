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

package dev.vxrp.bot.commands.handler.bot.help

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.time.Instant

class HelpCommand(val translation: Translation) {
    fun pasteHelpMenu(event: SlashCommandInteractionEvent) {
        event.replyEmbeds(pages()[0])
            .setActionRow(actionRow(0)).queue()
    }

    fun pages(): List<MessageEmbed> {
        val pages = mutableListOf<MessageEmbed>()

        pages.add(Embed {
            title = translation.help.embedPageOneTitle
            color = 0x000000
            description = ColorTool().parse(translation.help.embedPageOneBody).trimIndent()
            timestamp = Instant.now()
            footer(translation.help.embedFooterText, translation.help.embedFooterImg)
        })

        pages.add(Embed {
            title = translation.help.embedPageTwoTitle
            color = 0x000000
            description = ColorTool().parse(translation.help.embedPageTwoBody).trimIndent()
            timestamp = Instant.now()
            footer(translation.help.embedFooterText, translation.help.embedFooterImg)
        })
        pages.add(Embed {
            title = translation.help.embedPageThreeTitle
            color = 0x000000
            description = ColorTool().parse(translation.help.embedPageThreeBody).trimIndent()
            timestamp = Instant.now()
            footer(translation.help.embedFooterText, translation.help.embedFooterImg)
        })

        pages.add(Embed {
            title = translation.help.embedPageFourTitle
            color = 0x000000
            description = ColorTool().parse(translation.help.embedPageFourBody).trimIndent()
            timestamp = Instant.now()
            footer(translation.help.embedFooterText, translation.help.embedFooterImg)
        })

        pages.add(Embed {
            title = translation.help.embedPageFiveTitle
            color = 0x000000
            description = ColorTool().parse(translation.help.embedPageFiveBody).trimIndent()
            timestamp = Instant.now()
            footer(translation.help.embedFooterText, translation.help.embedFooterImg)
        })

        pages.add(Embed {
            title = translation.help.embedPageSixTitle
            color = 0x000000
            description = ColorTool().parse(translation.help.embedPageSixBody).trimIndent()
            timestamp = Instant.now()
            footer(translation.help.embedFooterText, translation.help.embedFooterImg)
        })

        return pages
    }

    fun actionRow(page: Int): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()
        if (page == 0) {
            rows.add(Button.danger("help_first_page", "|<").asDisabled())
            rows.add(Button.secondary("help_go_back:0", "<").asDisabled())
            rows.add(Button.link("https://github.com/Vxrpenter/SCPToolsBot/wiki", "📝 Documentation"))
            rows.add(Button.primary("help_go_forward:0", ">"))
            rows.add(Button.success("help_last_page:0", ">|"))
        }
        if (page == 5) {
            rows.add(Button.success("help_first_page", "|<"))
            rows.add(Button.primary("help_go_back:$page", "<"))
            rows.add(Button.link("https://github.com/Vxrpenter/SCPToolsBot/wiki", "📝 Documentation"))
            rows.add(Button.secondary("help_go_forward:$page", ">").asDisabled())
            rows.add(Button.danger("help_last_page:$page", ">|").asDisabled())
        }
        if (page != 0 && page != 5) {
            rows.add(Button.success("help_first_page", "|<"))
            rows.add(Button.primary("help_go_back:$page", "<"))
            rows.add(Button.link("https://github.com/Vxrpenter/SCPToolsBot/wiki", "📝 Documentation"))
            rows.add(Button.primary("help_go_forward:$page", ">"))
            rows.add(Button.success("help_last_page:$page", ">|"))
        }
        return rows
    }
}