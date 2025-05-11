package dev.vxrp.api.updates

import dev.vxrp.api.updates.data.Tag
import dev.vxrp.configuration.data.Config
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.color.enums.DCColor
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

class Updates(val config: Config) {
    private val logger: Logger = LoggerFactory.getLogger(Updates::class.java)
    private val client: OkHttpClient = OkHttpClient()

    fun checkForUpdatesByTag(url: String, log: Boolean = true): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return "none"

            val json = Json { ignoreUnknownKeys = true }
            val tagArray = json.decodeFromString<List<Tag>>(response.body?.string()!!)
            val tag = tagArray.first().ref.replace("refs/tags/v.", "")
            val downloadUrl = tagArray.first().url

            val properties = Properties()

            Updates::class.java.getResourceAsStream("/dev/vxrp/version.properties").use {
                versionPropertiesStream -> checkNotNull(versionPropertiesStream) { "Version properties file does not exist" }
                properties.load(InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8))
            }

            if (log) logger.info("Checking for latest version...")
            if (properties.getProperty("version") < tag) {
                if (config.settings.updates.ignoreBeta && tag.contains("beta", true)) return tag
                if (config.settings.updates.ignoreAlpha && tag.contains("alpha", true)) return tag

                if (log) logger.warn("A new version has been found, you can download it from {}", ColorTool().apply(DCColor.LIGHT_BLUE, downloadUrl))
                return tag
            } else {
                if (log) logger.info("Running latest build")
                return tag
            }
        }
    }
}