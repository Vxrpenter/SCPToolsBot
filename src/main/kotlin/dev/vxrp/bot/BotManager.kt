@file:Suppress("OPT_IN_USAGE")

package dev.vxrp.bot

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.listeners.CommandListener
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.events.ButtonListener
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.SqliteManager
import kotlinx.coroutines.*
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
        val commandManager = CommandManager(config, "/configs/commands.json")
        val statusManager = StatusManager(config, translation, "/configs/status.json")
        val sqliteManager = SqliteManager(config,"database", "data.db")

        commandManager.registerSpecificCommands(commandManager.query().commands, api)

        // This could cause memory leaks if the interaction is halted. But we can ignore it here because if status is active, this
        // Has to be launched. However, we first check if the status is active, so we can avoid these memory leaks, otherwise this has to
        // run all the time anyway

        if (statusManager.query().active) {
            GlobalScope.launch { statusManager.initialize(commandManager) }
        }


    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name}
    }
}