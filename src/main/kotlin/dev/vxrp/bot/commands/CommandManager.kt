package dev.vxrp.bot.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.slf4j.LoggerFactory

class CommandManager(val api: JDA) {
    private val logger = LoggerFactory.getLogger(CommandManager::class.java)
    private val commands = mutableListOf<CommandData>()

    fun registerCommand(command: CommandData) : CommandManager {
        commands.add(command)
        return this
    }

    fun initialize() {
        api.updateCommands().addCommands(commands)
        commands.forEach { command -> logger.info("Registering command ${command.name}")}
    }
}


