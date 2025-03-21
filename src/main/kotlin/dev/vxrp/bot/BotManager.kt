package dev.vxrp.bot

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.listeners.CommandListener
import dev.vxrp.bot.events.ButtonListener
import dev.vxrp.bot.events.EntitySelectListener
import dev.vxrp.bot.events.ModalListener
import dev.vxrp.bot.events.StringSelectListener
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.Timer
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

val timer = Timer()

class BotManager(val config: Config, val translation: Translation) {
    var mainApi: JDA? = null
    var mainCommandManager: CommandManager? = null

    fun init() {
        val api = light(config.settings.token, enableCoroutines = true) {
            intents += listOf(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
            setActivity(Activity.of(ActivityType.PLAYING.takeIf {
                !enumContains<ActivityType>(config.settings.activityType)
            } ?: ActivityType.valueOf(config.settings.activityType), config.settings.activityContent))
        }

        val launchOptionManager = LaunchOptionManager(config, translation)

        if (launchOptionManager.checkLaunchOption(LaunchOptionType.COMMAND_LISTENER).engage) api.addEventListener(CommandListener(api, config, translation))
        if (launchOptionManager.checkLaunchOption(LaunchOptionType.BUTTON_LISTENER).engage) api.addEventListener(ButtonListener(api, config, translation))
        if (launchOptionManager.checkLaunchOption(LaunchOptionType.STRING_SELECT_LISTENER).engage) api.addEventListener(StringSelectListener(api, config, translation))
        if (launchOptionManager.checkLaunchOption(LaunchOptionType.ENTITY_SELECT_LISTENER).engage) api.addEventListener(EntitySelectListener(api, config, translation))
        if (launchOptionManager.checkLaunchOption(LaunchOptionType.MODAL_LISTENER).engage) api.addEventListener(ModalListener(api, config, translation))

        if (launchOptionManager.checkLaunchOption(LaunchOptionType.COMMAND_MANAGER).engage) {
            val commandManager = CommandManager(config, "configs/commands.json")

            commandManager.registerSpecificCommands(commandManager.query().commands, api)
            mainCommandManager = commandManager
        }

        mainApi = api
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}