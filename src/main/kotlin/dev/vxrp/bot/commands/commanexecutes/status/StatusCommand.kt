package dev.vxrp.bot.commands.commanexecutes.status

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
        } else if (maintenance[currentPort] == false) {
            maintenance[currentPort!!] = true
            event.reply(ColorTool().useCustomColorCodes(translation.status.messageStatusActivated).trimIndent()).setEphemeral(true).queue()
        }
    }
}