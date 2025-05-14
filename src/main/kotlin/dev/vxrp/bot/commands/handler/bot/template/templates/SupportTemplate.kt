package dev.vxrp.bot.commands.handler.bot.template.templates

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class SupportTemplate(val config: Config, val translation: Translation) {
    fun pasteTemplate(event: SlashCommandInteractionEvent) {
        val embed = Embed {
            thumbnail = event.guild?.iconUrl
            title = ColorTool().useCustomColorCodes(translation.support.embedTemplateSupportTitle).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.support.embedTemplateSupportBody).trimIndent()
        }

        event.channel.send("", listOf(embed))
            .setActionRow(
                StringSelectMenu.create("ticket")
                    .addOption(translation.selectMenus.textSupportNameGeneral, "general", translation.selectMenus.textsupportDescriptionGeneral, Emoji.fromFormatted("⚙️"))
                    .addOption(translation.selectMenus.textSupportNameReport, "report", translation.selectMenus.textSupportDescriptionReport, Emoji.fromFormatted("⚖️"))
                    .addOption(translation.selectMenus.textSupportNameError, "error", translation.selectMenus.textSupportDescriptionError, Emoji.fromFormatted("⛓️‍💥"))
                    .addOption(translation.selectMenus.textSupportNameUnban, "unban", translation.selectMenus.textSupportDescriptionUnban, Emoji.fromFormatted("⌛"))
                    .addOption(translation.selectMenus.textSupportNameComplaint, "complaint", translation.selectMenus.textSupportDescriptionComplaint, Emoji.fromFormatted("🚫"))
                    .addOption(translation.selectMenus.textSupportNameApplication, "application", translation.selectMenus.textSupportDescriptionApplication, Emoji.fromFormatted("📩")).build()
            ).queue()
        event.reply("Created support template").setEphemeral(true).queue()
    }
}