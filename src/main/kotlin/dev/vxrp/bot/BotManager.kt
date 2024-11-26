package dev.vxrp.bot

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.CommandListener
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.events.ButtonListener
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.SqliteManager
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class BotManager(val config: Config, val translation: Translation) {
    init {
        val api = light(config.token, enableCoroutines=true) {
            intents += listOf(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            setActivity(Activity.of(
                ActivityType.PLAYING.takeIf { !enumContains<ActivityType>(config.activityType) } ?: ActivityType.valueOf(config.activityType),
                config.activityContent))
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
        }

        api.addEventListener(
            CommandListener(api, config, translation),
            ButtonListener(api, config, translation),
        )


        val guild = api.awaitReady().getGuildById(config.guildId)
        val commandManager = CommandManager(api, config, "/configs/commands.json").registerCommands()
        val statusManager = StatusManager(config, "/configs/status.json")
        val sqliteManager = SqliteManager(config,"database", "data.db")
        
        statusManager.initialize()
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name}
    }
}