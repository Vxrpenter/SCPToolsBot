package dev.vxrp.bot.commands.listeners

import dev.minn.jda.ktx.events.listener
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.commanexecutes.help.HelpCommand
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(val api: JDA, val config: Config, val translation: Translation) : ListenerAdapter() {
    init {
        api.listener<SlashCommandInteractionEvent> { event ->
            val commandManager = CommandManager(config, "configs/commands.json")

            val commandList = commandManager.query().commands

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

            "commands.template.default" -> templateCommand(event)

            "commands.notice_of_departure.default" -> noticeOfDepartureCommand(event)

            "commands.regulars.default" -> regularsCommand(event)
        }
    }

    private fun helpCommand(event: SlashCommandInteractionEvent) {
        HelpCommand(translation).pasteHelpMenu(event)
    }

    private fun templateCommand(event: SlashCommandInteractionEvent) {

    }

    private fun noticeOfDepartureCommand(event: SlashCommandInteractionEvent) {

    }

    private fun regularsCommand(event: SlashCommandInteractionEvent) {

    }

}