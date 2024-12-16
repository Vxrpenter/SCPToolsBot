package dev.vxrp.bot.commands.commanexecutes.template

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

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
        var unbanButton: Button = Button.danger("unban", ColorTool().useCustomColorCodes(translation.buttons.textUnbanCreateNewTicket).trimIndent()).withEmoji(Emoji.fromUnicode("📩"))

        if (config.cedmod.active) {
            event.hook.send("❌ Unban System is deactivated. To active, turn on 'CedMod Sync'").setEphemeral(true).queue()
            unbanButton = unbanButton.asDisabled()
        }

        event.channel.send("", listOf(support, unban))
            .setActionRow(
                Button.success("support", ColorTool().useCustomColorCodes(translation.buttons.textSupportCreateNewTicket).trimIndent()).withEmoji(Emoji.fromUnicode("📩")),
                unbanButton
            ).queue()
        event.reply("Created support template").setEphemeral(true).queue()
    }
}