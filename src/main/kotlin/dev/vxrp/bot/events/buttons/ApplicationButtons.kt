package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.application.applicationTypeMap
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class ApplicationButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.button.id?.startsWith("application_activation_add") == true && !nullCheck()) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionTitle)
                description = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("application_activation_add:${event.user.id}:${event.message.id}").also {
                    for (application in applicationTypeMap[event.user.id]!!) {
                        it.addOption(application.name, application.roleId, application.description, Emoji.fromFormatted(application.emoji))
                    }
                }.build()
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("application_activation_remove") == true && !nullCheck()) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionTitle)
                description = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("application_activation_remove:${event.user.id}").also {
                    for (application in applicationTypeMap[event.user.id]!!) {
                        it.addOption(application.name, application.roleId, application.description, Emoji.fromFormatted(application.emoji))
                    }
                }.build()
            ).setEphemeral(true).queue()
        }
    }

    private fun nullCheck(): Boolean {
        if (applicationTypeMap.isNotEmpty()) return false
        event.message.delete().queue()
        event.reply_("This message has expired, please execute the command again").setEphemeral(true).queue()

        return true
    }
}