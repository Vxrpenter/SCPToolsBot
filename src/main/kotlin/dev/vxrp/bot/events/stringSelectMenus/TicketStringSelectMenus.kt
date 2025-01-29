package dev.vxrp.bot.events.stringSelectMenus

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.modals.Support
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class TicketStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    init {
        val support = Support(translation)

        if (event.selectMenu.id?.startsWith("ticket") == true) {
            if (event.selectedOptions[0].value.startsWith("general")) {
                event.replyModal(support.supportGeneralModal()).queue()
            }

            if (event.selectedOptions[0].value.startsWith("report")) {
                val embed = Embed {
                    title = ColorTool().useCustomColorCodes(translation.support.embedReportUserTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedReportUserBody)
                }

                event.reply_("", listOf(embed)).addActionRow(
                    EntitySelectMenu.create("report", EntitySelectMenu.SelectTarget.USER).build())
                    .setEphemeral(true).queue()
            }

            if (event.selectedOptions[0].value.startsWith("error")) {
                event.replyModal(support.supportErrorModal()).queue()
            }

            if (event.selectedOptions[0].value.startsWith("unban")) {
                event.replyModal(support.supportUnbanModal()).queue()
            }

            if (event.selectedOptions[0].value.startsWith("complaint")) {
                val embed = Embed {
                    title = ColorTool().useCustomColorCodes(translation.support.embedComplaintUserTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedComplaintUserBody)
                }

                event.reply_("", listOf(embed)).addActionRow(
                    EntitySelectMenu.create("complaint", EntitySelectMenu.SelectTarget.USER).build())
                    .setEphemeral(true).queue()
            }

            if (event.selectedOptions[0].value.startsWith("application")) {
                val embed = Embed {
                    title = ColorTool().useCustomColorCodes(translation.support.embedApplicationPositionTitle)
                    description = ColorTool().useCustomColorCodes(translation.support.embedApplicationPositionBody)
                }

                event.reply_("", listOf(embed)).addActionRow(
                    StringSelectMenu.create("application_position").also {
                        for(type in config.ticket.applicationTypes) {
                            it.addOption(type.name, type.roleID, type.description, Emoji.fromFormatted(type.emoji))
                        }
                    }.build()
                ).setEphemeral(true).queue()
            }
        }

        if (event.selectMenu.id?.startsWith("application_position") == true) {
            event.replyModal(Support(translation).supportApplicationModal()).queue()
        }
    }
}