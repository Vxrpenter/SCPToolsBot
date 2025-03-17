package dev.vxrp.util.launch

import dev.vxrp.bot.BotManager
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.bot.timer
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.launch.data.LaunchArguments
import dev.vxrp.util.launch.enums.LaunchOptionType
import org.slf4j.LoggerFactory

class LaunchOptionManager(val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(LaunchOptionManager::class.java)

    fun startupBots() {
        logger.info("Launch configuration available, proceeding with startup...")

        val botOptions = checkLaunchOptions(LaunchOptionType.BOT)
        val clusterOptions = checkLaunchOptions(LaunchOptionType.STATUS_CLUSTER)

        if (botOptions.broken || clusterOptions.broken) {
            logger.error("Bot will not start unless configuration is fixed or you turn 'ignore_broken_entries' in 'launch-configuration.json' to true")
            logger.error("Shutting down...")
            return
        }

        val botManager = BotManager(config, translation)

        if (botOptions.engage) {
            botManager.init()
        }

        if (clusterOptions.engage) {
            val statusManager = StatusManager(botManager.mainApi!!, config, translation, timer, "configs/status-settings.json")
            statusManager.initialize(botManager.mainCommandManager!!)
        }
    }
    
    private fun checkLaunchOptions(type: LaunchOptionType): LaunchArguments {
        for (launchOption in config.launchConfiguration.order) {
            if (launchOption.id.split(":")[1] == type.toString()) return LaunchArguments(false, launchOption.engage, launchOption.separateThread)
        }

        if (!config.launchConfiguration.options.ignoreBrokenEntries) {
            logger.error("Could not find ${type}, missing entry. This could be a result of a broken launch configuration. Delete current configuration for it to be regenerated")
            return LaunchArguments(broken = true, engage = false, separateThread = false)
        }

        return LaunchArguments(broken = false, engage = false, separateThread = false)
    }
}