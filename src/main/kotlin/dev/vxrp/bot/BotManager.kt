package dev.vxrp.bot

import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.configuration.loaders.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.interactions.commands.build.Commands

class BotManager(val config: Config) {
    init {
        val api = JDABuilder.createDefault(config.token).build()

        initializeCommands(api)
    }

    private fun initializeCommands(api: JDA) {
        CommandManager(api, config)
            .registerCommand(Commands.slash("test1", "test1"))
            .registerCommand(Commands.slash("test2", "test2"))
            .registerCommand(Commands.slash("test3", "test3"))
            .registerCommand(Commands.slash("test4", "test4"))
            .initialize()
    }
}