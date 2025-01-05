package dev.vxrp.bot.events

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu

class StringSelectListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<StringSelectInteractionEvent> { event ->
            if (event.selectMenu.id?.startsWith("ticket") == true) {
                if (event.selectedOptions[0].value.startsWith("general")) {

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

                }

                if (event.selectedOptions[0].value.startsWith("unban")) {


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
            }
        }
    }
}