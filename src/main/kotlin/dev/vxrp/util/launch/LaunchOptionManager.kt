package dev.vxrp.util.launch

import dev.vxrp.bot.BotManager
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.bot.timer
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.launch.data.LaunchArguments
import dev.vxrp.util.launch.data.LaunchConfigurationOrder
import dev.vxrp.util.launch.enums.LaunchOptionSectionType
import dev.vxrp.util.launch.enums.LaunchOptionType
import dev.vxrp.web.WebServerManager
import org.slf4j.LoggerFactory

class LaunchOptionManager(val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(LaunchOptionManager::class.java)

    fun startupBots() {
        logger.info("Launch configuration available, proceeding with startup...")

        val botOptions = checkLaunchOption(LaunchOptionType.BOT)
        val clusterOptions = checkLaunchOption(LaunchOptionType.STATUS_CLUSTER)

        if (botOptions.broken || clusterOptions.broken) {
            logger.error("Bot will not start unless configuration is fixed or you turn 'ignore_broken_entries' in 'launch-configuration.json' to true")
            logger.error("Shutting down...")
            return
        }

        val botManager = BotManager(config, translation)

        if (botOptions.engage) {
            botManager.init()
        } else {
            logger.error("Because main bot is disabled, any other launches will be canceled")
        }

        if (botManager.mainCommandManager == null) {
            logger.error("Command Manager must be engaged for Status bots to work")
            return
        }
        if (clusterOptions.engage && botOptions.engage) {
            val statusManager = StatusManager(botManager.mainApi!!, config, translation, timer, "SCPToolsBot/configs/status-settings.yml")
            statusManager.initialize(botManager.mainCommandManager!!)
        }

        if (config.settings.webserver.active) WebServerManager(botManager.mainApi!!, config, translation)
    }

    fun checkSectionOption(type: LaunchOptionType, sectionType: LaunchOptionSectionType): LaunchArguments {
        val optionCheck = checkLaunchOption(type)
        if (!optionCheck.engage || optionCheck.broken) return LaunchArguments(false, engage = false)

        var currentLaunchOption: LaunchConfigurationOrder? = null

        for (launchOption in config.extra.launchConfiguration.order) {
            if (launchOption.id.split(":")[1] == type.toString()) currentLaunchOption = launchOption
        }

        for (sectionOption in currentLaunchOption!!.sections!!) {
            if (sectionOption.id.split(":")[1] == sectionType.toString()) {
                if (sectionOption.logAction) logger.debug("Launching section {} of {}", sectionType, type)
                return LaunchArguments(broken = false, engage = true)
            }
        }

        if (!config.extra.launchConfiguration.options.ignoreBrokenEntries) {
            logger.error("Could not find $sectionType section in $type missing entry. This could be a result of a broken launch configuration. Delete current configuration for it to be regenerated")
            return LaunchArguments(broken = true, engage = false)
        }

        return LaunchArguments(false, engage = false)
    }

    fun checkLaunchOption(type: LaunchOptionType): LaunchArguments {
        for (launchOption in config.extra.launchConfiguration.order) {
            if (launchOption.id.split(":")[1] == type.toString()) return LaunchArguments(false, launchOption.engage)
        }

        if (!config.extra.launchConfiguration.options.ignoreBrokenEntries) {
            logger.error("Could not find $type, missing entry. This could be a result of a broken launch configuration. Delete current configuration for it to be regenerated")
            return LaunchArguments(broken = true, engage = false)
        }

        return LaunchArguments(broken = false, engage = false)
    }
}