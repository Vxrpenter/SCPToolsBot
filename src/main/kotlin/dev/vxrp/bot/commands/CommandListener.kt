package dev.vxrp.bot.commands

import dev.minn.jda.ktx.events.listener
import dev.vxrp.configuration.loaders.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(val api: JDA, val config: Config) : ListenerAdapter() {
    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            val commandList = CommandManager(api, config, "/configs/commands.json").query().commands

            for (command in commandList) {
                if (command.name.equals(event.name, ignoreCase = true)) continue
                checkInheritance(command.inherit)
            }
        }
    }

    private fun checkInheritance(inherit: String) {
        println(inherit)
        when (inherit) {
            "commands.help.default" -> helpCommand()

            "commands.template.default" -> templateCommand()

            "commands.notice_of_departure.default" -> noticeOfDepartureCommand()

            "commands.regulars.default" -> regularsCommand()
        }
    }

    private fun helpCommand() {

    }

    private fun templateCommand() {

    }

    private fun noticeOfDepartureCommand() {

    }

    private fun regularsCommand() {

    }
}