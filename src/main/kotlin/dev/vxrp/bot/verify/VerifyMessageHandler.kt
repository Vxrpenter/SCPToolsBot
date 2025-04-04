package dev.vxrp.bot.verify

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory

class VerifyMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(VerifyMessageHandler::class.java)

    fun sendVerificationMessage(username: String) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.verify.embedLogVerifiedTitle
                .replace("%name%", username))
            description = ColorTool().useCustomColorCodes(translation.verify.embedLogVerifiedBody)
        }

        val channel = api.getTextChannelById(config.settings.verify.verifyLogChannel)
        channel?.send("", listOf(embed))?.queue() ?: run {
            logger.error("Could not correctly retrieve verify log channel, does it exist?")
            return
        }
    }

    fun sendDeletionMessage(username: String) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.verify.embedLogDeletedTitle
                .replace("%name%", username))
            description = ColorTool().useCustomColorCodes(translation.verify.embedLogDeletedBody)
        }

        val channel = api.getTextChannelById(config.settings.verify.verifyLogChannel)
        channel?.send("", listOf(embed))?.queue() ?: run {
            logger.error("Could not correctly retrieve verify log channel, does it exist?")
            return
        }
    }
}