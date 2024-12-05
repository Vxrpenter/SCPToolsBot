package dev.vxrp.api.github

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import dev.vxrp.util.enums.DCColor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

class Github {
    private val logger: Logger = LoggerFactory.getLogger(Github::class.java)
    private val client: OkHttpClient = OkHttpClient()

    fun checkForUpdatesByTag(url: String, log: Boolean = true): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return "null"

            var tagArray: JsonArray? = null
            try {
                tagArray = JsonParser.parseString(response.body?.string()).asJsonArray
            } catch (e: IllegalStateException) {
                logger.error("Not able to get latest version from github, probably exceeded rate limit... skipping")
                return "null"
            }

            val tag = tagArray[tagArray.size() - 1].asJsonObject["ref"].asString
                .replace("refs/tags/v.", "")

            val properties = Properties()

            Github::class.java.getResourceAsStream("/dev/vxrp/version.properties").use { versionPropertiesStream ->
                checkNotNull(versionPropertiesStream) { "Version properties file does not exist" }
                properties.load(InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8))
            }
            if (log) logger.info("Checking for latest version...")
            if (properties.getProperty("version") < tag) {
                if (log) logger.warn(
                    "A new version has been found, you can download it from {}", dev.vxrp.util.color.ColorTool().apply(
                        DCColor.LIGHT_BLUE,
                        "https://github.com/Vxrpenter/SCPToolsBot/releases/tag/v.$tag"
                    )
                )
                return tag
            } else {
                if (log) logger.info("Running latest build")
                return tag
            }
        }
    }
}