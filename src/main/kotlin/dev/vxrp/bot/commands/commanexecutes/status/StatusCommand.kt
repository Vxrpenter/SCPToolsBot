package dev.vxrp.bot.commands.commanexecutes.status

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatusCommand(val config: Config, val translation: Translation, private val statusConstructor: StatusConstructor) {
    fun changeMaintenanceState(event: SlashCommandInteractionEvent) {
        val currentPort = statusConstructor.mappedBots[event.jda.selfUser.id]
        val maintenance = statusConstructor.maintenance

        if (maintenance[currentPort] == null) {
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusEmpty).trimIndent())
                .setEphemeral(true).queue()
        }

        if (maintenance[currentPort] == true) {
            var reason: String = translation.status.embedMaintenanceOffReasonFieldValue
            if (event.getOption("reason")?.asString != null) {
                reason = event.getOption("reason")?.asString!!
            }

            maintenance[currentPort!!] = false
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusDeactivated).trimIndent())
                .setEphemeral(true).queue()

            val embed = Embed {
                color = 0x2ECC70
                url = config.status.pageUrl
                title = ColorTool().useCustomColorCodes(
                    translation.status.embedMaintenanceOffTitle
                        .replace("%instance%", statusConstructor.instance.name)
                ).trimIndent()
                description = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffBody).trimIndent()
                field {
                    name = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffReasonFieldName).trimIndent()
                    value =
                        ColorTool().useCustomColorCodes(reason).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        } else if (maintenance[currentPort] == false) {
            var reason: String = translation.status.embedMaintenanceOnReasonFieldValue
            if (event.getOption("reason")?.asString != null) {
                reason = event.getOption("reason")?.asString!!
            }

            maintenance[currentPort!!] = true
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusActivated).trimIndent())
                .setEphemeral(true).queue()

            val embed = Embed {
                color = 0xf1c40f
                url = config.status.pageUrl
                title = ColorTool().useCustomColorCodes(
                    translation.status.embedMaintenanceOnTitle
                        .replace("%instance%", statusConstructor.instance.name)
                ).trimIndent()
                description = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnBody).trimIndent()
                field {
                    name = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnReasonFieldName).trimIndent()
                    value =
                        ColorTool().useCustomColorCodes(reason).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        }
    }
}