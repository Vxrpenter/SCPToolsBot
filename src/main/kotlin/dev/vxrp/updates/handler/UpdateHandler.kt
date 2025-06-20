package dev.vxrp.updates.handler

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.storage.ConfigPaths
import dev.vxrp.updates.UpdateManager
import dev.vxrp.updates.data.Tag
import dev.vxrp.updates.data.Updates
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.color.enums.DCColor
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.io.path.Path

class UpdateHandler() {
    private val logger = LoggerFactory.getLogger(UpdateHandler::class.java)
    private val client: OkHttpClient = OkHttpClient()
    private val dir = System.getProperty("user.dir")

    fun checkUpdated(old: Updates, new: Updates, force: Boolean) {
        if (old.version == new.version) if (!force) return

        logger.warn("We have detected that you have installed an update for SCPToolsBot. The bot will now run an update check to see if your configurations are still up to date...")

        if (new.configurationUpdate.config) {
            logger.warn("Your config.yml is out of date, it's structure has been altered in the last update. Please look at the most current version of the config.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/config.yml")
            if (old.settings.regenerateConfig) {
                logger.warn("You have activated 'regenerateConfig', config.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().configPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.statusSettings) {
            logger.warn("Your status-settings.yml is out of date, it's structure has been altered in the last update. Please look at the most current version of the status-settings.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/status-settings.yml")
            if (old.settings.regenerateStatusSettings) {
                logger.warn("You have activated 'regenerateStatusSettings', status-settings.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().statusPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.ticketSettings) {
            logger.warn("Your ticket-settings.yml is out of date, it's structure has been altered in the last update. Please look at the most current version of the ticket-settings.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/ticket-settings.yml")
            if (old.settings.regenerateTicketSettings) {
                logger.warn("You have activated 'regenerateTicketSettings', ticket-settings.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().ticketPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.extra.commands) {
            logger.warn("Your commands.json is out of date, it's structure has been altered in the last update. Please look at the most current version of the commands.json here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/extra/commands.json")
            if (old.settings.regenerateCommands) {
                logger.warn("You have activated 'regenerateCommands', commands.json file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().commandsPath}").toFile()
                file.delete()
            }
        }

        if (new.configurationUpdate.extra.launchConfiguration) {
            logger.warn("Your launch-configuration.json is out of date, it's structure has been altered in the last update. Please look at the most current version of the launch-configuration.json here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/configs/extra/launch-configuration.json")
            if (old.settings.regenerateLaunchConfiguration) {
                logger.warn("You have activated 'regenerateLaunchConfiguration', launch-configuration.json file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().launchConfigurationPath}").toFile()
                file.delete()
            }
        }

        if (new.translationUpdates.enUs) {
            logger.warn("Your en_US.yml translation is out of date, it's structure has been altered in the last update. Please look at the most current version of the en_US.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/lang/en_US.yml")
            if (old.settings.regenerateTranslations) {
                logger.warn("You have activated 'regenerateTranslations', en_US.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().enUsPath}").toFile()
                file.delete()
            }
        }

        if (new.translationUpdates.deDe) {
            logger.warn("Your de_DE.yml translation is out of date, it's structure has been altered in the last update. Please look at the most current version of the de_DE.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/lang/de_DE.yml")
            if (old.settings.regenerateTranslations) {
                logger.warn("You have activated 'regenerateTranslations', de_DE.yml file will be deleted and regenerated")
                val file = Path("$dir${ConfigPaths().deDePath}").toFile()
                file.delete()
            }
        }

        if (new.regularsUpdate.config) {
            logger.warn("Your regulars config.yml's are out of date, their structure been altered in the last update. Please look at the most current version of the config.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/regulars/example/config.yml")
        }

        if (new.regularsUpdate.manifest) {
            logger.warn("Your regulars manifest.yml's are out of date, their structure been altered in the last update. Please look at the most current version of the manifest.yml here: https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/resources/SCPToolsBot/regulars/example/manifest.yml")
        }

        if (new.additionalInformation != "") logger.warn("Additional Information: ${new.additionalInformation}")
    }

    fun checkForUpdatesByTag(config: Config, url: String): String {
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                logger.warn("Could not fetch newest version from GitHub")
                return "None"
            }

            val json = Json { ignoreUnknownKeys = true }
            val tagArray = json.decodeFromString<List<Tag>>(response.body?.string()!!)
            val latestPreRelease = tagArray.last().ref.replace("refs/tags/v.", "").replace("refs/tags/v", "")
            val latestRelease = tagArray.first().ref.replace("refs/tags/v.", "").replace("refs/tags/v", "")

            var fullTag: String
            var tag: String
            var preReleaseType = ""
            var preReleaseNumber = 0
            if (chooseLatestRelease(latestRelease, latestPreRelease)) {
                val splitArray = latestPreRelease.split("-")

                fullTag = latestPreRelease
                tag = splitArray.first()
                preReleaseNumber = splitArray.last().replace("alpha", "").replace("beta", "").toInt()
                preReleaseType = splitArray.last().replace(preReleaseNumber.toString(), "")
            } else {
                fullTag = latestRelease
                tag = latestRelease
            }

            val downloadUrl = "https://github.com/Vxrpenter/SCPToolsBot/releases/tag/v$fullTag"
            val properties = Properties()

            UpdateManager::class.java.getResourceAsStream("/dev/vxrp/version.properties").use {
                    versionPropertiesStream -> checkNotNull(versionPropertiesStream) { "Version properties file does not exist" }
                properties.load(InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8))
            }

            logger.info("Checking for latest version...")
            if (properties.getProperty("version") != fullTag && properties.getProperty("version").split("-").first() <= tag) {
                if (properties.getProperty("version").contains("alpha") || properties.getProperty("version").contains("beta")) {
                    val propertiesNumber = properties.getProperty("version").split("-").last().replace("alpha", "").replace("beta", "")
                    val propertiesPreReleaseType = properties.getProperty("version").split("-").last().replace(propertiesNumber, "")

                    if (propertiesPreReleaseType == "alpha" && preReleaseType != "beta" || propertiesPreReleaseType == "beta" && preReleaseType == "beta") {
                        if (propertiesNumber.toInt() > preReleaseNumber) return "None"
                    }
                }

                if (config.settings.updates.ignoreBeta && tag.contains("beta", true)) return tag
                if (config.settings.updates.ignoreAlpha && tag.contains("alpha", true)) return tag

                logger.warn("A new version has been found, you can download it from {}", ColorTool().apply(DCColor.LIGHT_BLUE, downloadUrl))
                return tag
            } else {
                logger.info("Running latest build")
                return tag
            }
        }
    }

    private fun chooseLatestRelease(release: String, preRelease: String): Boolean {
        val preReleaseSplit = preRelease.split("-").first()

        return preReleaseSplit >= release
    }
}