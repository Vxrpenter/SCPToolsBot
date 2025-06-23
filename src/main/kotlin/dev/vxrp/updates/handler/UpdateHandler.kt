/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.updates.handler

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.storage.ConfigPaths
import dev.vxrp.updates.UpdateManager
import dev.vxrp.updates.data.Tag
import dev.vxrp.updates.data.Updates
import dev.vxrp.updates.data.UpdatesConfigurationSegment
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.color.enums.DCColor
import dev.vxrp.util.upstreamVersion
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

    fun checkUpdated(old: Updates, new: Updates) {
        if (old.version == new.version) return

        val changedMessage = "Your {} {} is out of date, it's structure has been altered in the last update. Please look at the most current version of the {} here: {}"
        val regenerateMessage = "You have activated 'regenerate' for {} file, it will be regenerated"

        logger.warn("We have detected that you have installed an update for SCPToolsBot. The bot will now run an update check to see if your configurations are still up to date...")

        val communalList = mutableListOf<UpdatesConfigurationSegment>()
        communalList.addAll(new.configurationUpdate)
        communalList.addAll(new.translationUpdates)
        communalList.addAll(new.regularsUpdate)

        var changed = false
        for (config in communalList) {
            if (!config.changed) continue
            changed = true
            logger.warn(changedMessage, config.filename, config.type, config.filename, config.upstream)
            if (!config.regenerate) continue
            logger.warn(regenerateMessage, config.filename)
            val file = Path("$dir${config.location}").toFile()
            file.delete()
        }

        if (!changed) logger.info("No configuration files were changed in this update")
        if (new.additionalInformation.isNotBlank()) logger.warn("Additional Information: ${new.additionalInformation}")
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
            var preReleaseNumber = "0"
            if (chooseLatestRelease(latestRelease, latestPreRelease)) {
                val splitArray = latestPreRelease.split("-")

                fullTag = latestPreRelease
                tag = splitArray.first()
                preReleaseNumber = splitArray.last().replace("alpha", "").replace("beta", "")
                preReleaseType = splitArray.last().replace(preReleaseNumber, "")
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
                        val propertiesSplit = propertiesNumber.split(".")
                        val preReleaseSplit = preReleaseNumber.split(".")
                        if (propertiesSplit.first() > preReleaseSplit.first() && propertiesSplit[1] > preReleaseSplit[1] && propertiesSplit.last() > preReleaseSplit.last()) return "None"
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