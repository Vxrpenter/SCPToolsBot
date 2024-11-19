package dev.vxrp.bot

import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.configuration.loaders.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

class BotManager(val config: Config) {
    init {
        val api = JDABuilder.createDefault(config.token).build()

        initializeCommands(api)
    }

    private fun initializeCommands(api: JDA) {

        CommandManager(api, config, "/configs/commands.json").registerCommands()
    }
}