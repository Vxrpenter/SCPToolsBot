package dev.vxrp.bot

import dev.minn.jda.ktx.interactions.components.StringSelectMenu
import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.listeners.CommandListener
import dev.vxrp.bot.events.ButtonListener
import dev.vxrp.bot.events.EntitySelectListener
import dev.vxrp.bot.events.ModalListener
import dev.vxrp.bot.events.StringSelectListener
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.SqliteManager
import dev.vxrp.util.Timer
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

val timer = Timer()

class BotManager(val config: Config, val translation: Translation) {
    init {
        val api = light(config.settings.token, enableCoroutines = true) {
            intents += listOf(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            setActivity(Activity.of(
                ActivityType.PLAYING.takeIf { !enumContains<ActivityType>(config.settings.activityType) }
                    ?: ActivityType.valueOf(config.settings.activityType),
                config.settings.activityContent))
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
        }

        api.addEventListener(
            CommandListener(api, config, translation),
            ButtonListener(api, config, translation),
            StringSelectListener(api, config, translation),
            EntitySelectListener(api, config, translation),
            ModalListener(api, config, translation)
        )

        val guild = api.awaitReady().getGuildById(config.settings.guildId)
        val commandManager = CommandManager(config, "configs/commands.json")
        val statusManager = StatusManager(api, config, translation, timer, "configs/status-settings.json")
        val sqliteManager = SqliteManager(config, "database", "data.db")

        commandManager.registerSpecificCommands(commandManager.query().commands, api)

        statusManager.initialize(commandManager)
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}