package dev.vxrp.bot.commands

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.commands.help.HelpCommand
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            val commandList = CommandManager(api, config, "/configs/commands.json").query().commands

            for (command in commandList) {
                if (command.name != event.fullCommandName) continue
                checkInheritance(command.inherit, event)
                break
            }
        }
    }

    private fun checkInheritance(inherit: String, event: SlashCommandInteractionEvent) {
        when (inherit) {
            "commands.help.default" -> helpCommand(event)

            "commands.template.default" -> templateCommand()

            "commands.notice_of_departure.default" -> noticeOfDepartureCommand()

            "commands.regulars.default" -> regularsCommand()
        }
    }

    private fun helpCommand(event: SlashCommandInteractionEvent) {
        HelpCommand(translation).pasteHelpMenu(event)
    }

    private fun templateCommand() {

    }

    private fun noticeOfDepartureCommand() {

    }

    private fun regularsCommand() {

    }
}