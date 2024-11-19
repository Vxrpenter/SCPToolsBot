package dev.vxrp.bot

import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.SqliteManager
import net.dv8tion.jda.api.JDABuilder

class BotManager(val config: Config) {
    init {
        val api = JDABuilder.createDefault(config.token).build()

        CommandManager(api, config, "/configs/commands.json").registerCommands()
        val sqliteManager = SqliteManager("database", "data.db")

        sqliteManager.initializeTickets()
    }



}