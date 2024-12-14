package dev.vxrp.bot

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.listeners.CommandListener
import dev.vxrp.bot.events.ButtonListener
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.SqliteManager
import dev.vxrp.util.Timer
import kotlinx.coroutines.*
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.LoggerFactory

class BotManager(val config: Config, val translation: Translation) {
    private val timer = Timer()

    init {
        val api = light(config.token, enableCoroutines = true) {
            intents += listOf(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            setActivity(Activity.of(
                ActivityType.PLAYING.takeIf { !enumContains<ActivityType>(config.activityType) }
                    ?: ActivityType.valueOf(config.activityType),
                config.activityContent))
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
        }

        api.addEventListener(
            CommandListener(api, config, translation),
            ButtonListener(api, config, translation),
        )

        val guild = api.awaitReady().getGuildById(config.guildId)
        val commandManager = CommandManager(config, "configs/commands.json")
        val statusManager = StatusManager(api, config, translation, timer, "configs/status.json")
        val sqliteManager = SqliteManager(config, "database", "data.db")

        commandManager.registerSpecificCommands(commandManager.query().commands, api)

        val statusScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
            LoggerFactory.getLogger(javaClass).error("An error occurred in the timer status coroutine", exception)
        }) + SupervisorJob()

        if (statusManager.query().active) {
            statusScope.launch { statusManager.initialize(commandManager) }
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}