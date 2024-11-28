package dev.vxrp.bot.commands.commanexecutes.status

import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatusCommand(val config: Config, val translation: Translation, private val statusConst: StatusConst) {

    fun changeMaintenanceState(event: SlashCommandInteractionEvent) {
        val currentPort = statusConst.mappedBots[event.jda.selfUser.id]
        val maintenance = statusConst.maintenance

        println(maintenance)
        if (maintenance[currentPort] == null) {
            event.reply("There is no Statusbot running").setEphemeral(true).queue()
        }

        if (maintenance[currentPort] == true) {
            maintenance[currentPort!!] = false
            event.reply("Deactivated maintenance mode for server").setEphemeral(true).queue()
        } else if (maintenance[currentPort] == false) {
            maintenance[currentPort!!] = true
            event.reply("Switched server to maintenance mode").setEphemeral(true).queue()
        }
    }
}