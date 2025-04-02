package dev.vxrp.bot.noticeofdeparture

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.NoticeOfDepartureTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoticeOfDepartureManager(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(NoticeOfDepartureManager::class.java)

    fun sendDecisionMessage(userId: String, date: String, reason: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.now().format(formatter)
        val endDate = LocalDate.parse(date, formatter).format(formatter)

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDescisionTitle
                .replace("%number%", NoticeOfDepartureTable().retrieveSerial().toString()))
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDescisionBody
                .replace("%current_date%", currentDate.toString())
                .replace("%end_date%", endDate.toString())
                .replace("%reason%", reason))
            timestamp = Instant.now()
        }

        val channel: TextChannel? = api.getTextChannelById(config.settings.noticeOfDeparture.decisionChannel)
        channel?.send("", listOf(embed))?.addActionRow(
            Button.success("notice_of_departure_decision_accept", translation.buttons.textNoticeOfDepartureAccept).withEmoji(Emoji.fromFormatted("📩")),
            Button.danger("notice_of_departure_decision_dismiss", translation.buttons.textNoticeOfDepartureDismissed).withEmoji(Emoji.fromFormatted("🫷"))
        )?.queue() ?: run{
            logger.error("Could not correctly retrieve notice of departure decision channel")
            return
        }
    }

    fun sendAcceptedMessage() {

    }

    fun sendDismissedMessage() {

    }

    fun sendNoticeMessage() {

    }

    fun sendRevokedMessage() {

    }
}