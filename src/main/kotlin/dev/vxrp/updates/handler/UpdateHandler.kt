package dev.vxrp.updates.handler

import dev.vxrp.configuration.storage.ConfigPaths
import dev.vxrp.updates.data.Updates
import org.slf4j.LoggerFactory
import kotlin.io.path.Path

class UpdateHandler(val old: Updates, val new: Updates) {
    private val logger = LoggerFactory.getLogger(UpdateHandler::class.java)
    private val dir = System.getProperty("user.dir")

    fun checkUpdated() {
        if (old.version == new.version) if (!new.force) return

        logger.info("We have detected that you have installed an update for ScpTools. The bot will now run an update check to see if your configurations are still up to date...")

        if (new.configurationUpdate.config) {
            logger.warn("Your config.yml is out of date, it has been altered in the last update. Please look at the most current version of the config.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/config.yml")
            if (old.settings.regenerateConfig) {
                logger.warn("You have activated 'regenerateConfig', config.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().configPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.statusSettings) {
            logger.warn("Your status-settings.yml is out of date, it has been altered in the last update. Please look at the most current version of the status-settings.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/status-settings.yml")
            if (old.settings.regenerateStatusSettings) {
                logger.warn("You have activated 'regenerateStatusSettings', status-settings.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().statusPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.ticketSettings) {
            logger.warn("Your ticket-settings.yml is out of date, it has been altered in the last update. Please look at the most current version of the ticket-settings.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/ticket-settings.yml")
            if (old.settings.regenerateTicketSettings) {
                logger.warn("You have activated 'regenerateTicketSettings', ticket-settings.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().ticketPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.extra.commands) {
            logger.warn("Your commands.json is out of date, it has been altered in the last update. Please look at the most current version of the commands.json here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/extra/commands.json")
            if (old.settings.regenerateCommands) {
                logger.warn("You have activated 'regenerateCommands', commands.json file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().commandsPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.extra.launchConfiguration) {
            logger.warn("Your launch-configuration.json is out of date, it has been altered in the last update. Please look at the most current version of the launch-configuration.json here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/extra/launch-configuration.json")
            if (old.settings.regenerateLaunchConfiguration) {
                logger.warn("You have activated 'regenerateLaunchConfiguration', launch-configuration.json file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().launchConfigurationPath}").toFile()
                file.delete()
            }
        }

        if (new.translationUpdates.enUs) {
            logger.warn("Your en_US.yml translation is out of date, it has been altered in the last update. Please look at the most current version of the en_US.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/lang/en_US.yml")
            if (old.settings.regenerateTranslations) {
                logger.warn("You have activated 'regenerateTranslations', en_US.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().enUsPath}").toFile()
                file.delete()
            }
        }

        if (new.translationUpdates.deDe) {
            logger.warn("Your de_DE.yml translation is out of date, it has been altered in the last update. Please look at the most current version of the de_DE.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/lang/de_DE.yml")
            if (old.settings.regenerateTranslations) {
                logger.warn("You have activated 'regenerateTranslations', deDE.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().deDePath}").toFile()
                file.delete()
            }
        }

        if (new.regularsUpdate.config) {
            logger.warn("Your regulars config.yml's are out of date, it has been altered in the last update. Please look at the most current version of the config.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/regulars/example/config.yml")
        }

        if (new.regularsUpdate.manifest) {
            logger.warn("Your regulars manifest.yml's are out of date, it has been altered in the last update. Please look at the most current version of the manifest.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/regulars/example/manifest.yml")
        }

        if (new.additionalInformation != "") logger.warn("Additional Information: ${new.additionalInformation}")
    }
}