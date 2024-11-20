package dev.vxrp.bot

import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.SqliteManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class BotManager(val config: Config) {
    init {
        val api = JDABuilder.createDefault(config.token, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).apply {
            setActivity(Activity.of(
                ActivityType.PLAYING.takeIf { !enumContains<ActivityType>(config.activityType) } ?: ActivityType.valueOf(config.activityType),
                config.activityContent))
        }
            .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
            .build()

        CommandManager(api, config, "/configs/commands.json").registerCommands()
        SqliteManager(config,"database", "data.db")

    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name}
    }
}