package dev.vxrp.bot.commands

import dev.vxrp.configuration.loaders.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.slf4j.LoggerFactory

class CommandManager(val api: JDA, val config: Config) {
    private val logger = LoggerFactory.getLogger(CommandManager::class.java)
    private val commands = mutableListOf<CommandData>()

    fun registerCommand(command: CommandData) : CommandManager {
        commands.add(command)
        return this
    }

    fun initialize() {
        for (command in commands) {
            if (!config.commands.contains(command.name)) continue
            api.updateCommands().addCommands(command)
            logger.info("Registering command ${command.name}")
        }
    }
}


