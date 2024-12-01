package dev.vxrp.bot.commands.commanexecutes.status

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatusCommand(val config: Config, val translation: Translation, private val statusConst: StatusConst) {

    fun changeMaintenanceState(event: SlashCommandInteractionEvent) {
        val currentPort = statusConst.mappedBots[event.jda.selfUser.id]
        val maintenance = statusConst.maintenance

        if (maintenance[currentPort] == null) {
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusEmpty).trimIndent()).setEphemeral(true).queue()
        }

        if (maintenance[currentPort] == true) {
            maintenance[currentPort!!] = false
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusDeactivated).trimIndent()).setEphemeral(true).queue()

            val embed = Embed {
                color = 0x2ECC70
                url = config.status.pageUrl
                title = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffTitle
                    .replace("%instance%", statusConst.instance.name)).trimIndent()
                description = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffBody).trimIndent()
                field {
                    name = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffFieldName).trimIndent()
                    value = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffFieldValue).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        } else if (maintenance[currentPort] == false) {
            maintenance[currentPort!!] = true
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusActivated).trimIndent()).setEphemeral(true).queue()

            val embed = Embed {
                color = 0xE74D3C
                url = config.status.pageUrl
                title = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnTitle
                    .replace("%instance%", statusConst.instance.name)).trimIndent()
                description = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnBody).trimIndent()
                field {
                    name = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnFieldName).trimIndent()
                    value = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnFieldValue).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        }
    }
}