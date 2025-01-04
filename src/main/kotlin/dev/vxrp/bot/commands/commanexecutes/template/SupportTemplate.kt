package dev.vxrp.bot.commands.commanexecutes.template

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class SupportTemplate(val config: Config, val translation: Translation) {
    fun pastePaste(event: SlashCommandInteractionEvent) {
        val support = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTemplateSupportTitle).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.support.embedTemplateSupportBody).trimIndent()
        }
        val unban = Embed {
            title = ColorTool().useCustomColorCodes(translation.support.embedTemplateUnbanTitle).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.support.embedTemplateUnbanBody).trimIndent()
        }

        event.channel.send("", listOf(support, unban))
            .setActionRow(
                StringSelectMenu.create("ticket")
                    .addOption(translation.selectMenus.textSupportNameGeneral, "general", translation.selectMenus.textsupportDescriptionGeneral, Emoji.fromFormatted("⚙️"))
                    .addOption(translation.selectMenus.textSupportNameReport, "report", translation.selectMenus.textSupportDescriptionReport, Emoji.fromFormatted("⚖️"))
                    .addOption(translation.selectMenus.textSupportNameError, "error", translation.selectMenus.textSupportDescriptionError, Emoji.fromFormatted("⛓️‍💥"))
                    .addOption(translation.selectMenus.textSupportNameUnban, "unban", translation.selectMenus.textSupportDescriptionUnban, Emoji.fromFormatted("⌛"))
                    .addOption(translation.selectMenus.textSupportNameComplaint, "complaint", translation.selectMenus.textSupportDescriptionComplaint, Emoji.fromFormatted("🚫")).build()
            ).queue()
        event.reply("Created support template").setEphemeral(true).queue()
    }
}