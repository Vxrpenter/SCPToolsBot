package dev.vxrp.bot.noticeofdeparture

import dev.minn.jda.ktx.coroutines.await
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
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionTitle
                .replace("%number%", NoticeOfDepartureTable().retrieveSerial().toString()))
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDecisionBody
                .replace("%current_date%", currentDate.toString())
                .replace("%end_date%", endDate.toString())
                .replace("%reason%", reason))
            timestamp = Instant.now()
        }

        val channel: TextChannel? = api.getTextChannelById(config.settings.noticeOfDeparture.decisionChannel)
        channel?.send("", listOf(embed))?.addActionRow(
            Button.success("notice_of_departure_decision_accept:$userId$endDate", translation.buttons.textNoticeOfDepartureAccept).withEmoji(Emoji.fromFormatted("📩")),
            Button.danger("notice_of_departure_decision_dismiss$userId$endDate", translation.buttons.textNoticeOfDepartureDismissed).withEmoji(Emoji.fromFormatted("🫷"))
        )?.queue() ?: run{
            logger.error("Could not correctly retrieve notice of departure decision channel, does it exist?")
            return
        }
    }

    suspend fun sendAcceptedMessage(reason: String, userId: String, date: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.now().format(formatter)
        val endDate = LocalDate.parse(date, formatter).format(formatter)

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptedTitle)
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptedBody
                .replace("%current_date%", currentDate)
                .replace("%end_date", endDate)
                .replace("%reason%", reason))
        }

        val privateChannel = api.retrieveUserById(userId).await().openPrivateChannel().await()
        privateChannel.send("", listOf(embed)).queue()
    }

    suspend fun sendDismissedMessage(reason: String, userId: String) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissedTitle)
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissedBody
                .replace("%reason%", reason))
        }

        val privateChannel = api.retrieveUserById(userId).await().openPrivateChannel().await()
        privateChannel.send("", listOf(embed)).queue()
    }

    suspend fun sendNoticeMessage(reason: String, userId: String, date: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.now().format(formatter)
        val endDate = LocalDate.parse(date, formatter).format(formatter)

        val user = api.retrieveUserById(userId).await()

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNoticeTitle
                .replace("%number%", NoticeOfDepartureTable().retrieveSerial().toString()))
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedNoticeBody
                .replace("%user%", user.asMention)
                .replace("%current_date%", currentDate)
                .replace("%end_date", endDate)
                .replace("%reason%", reason))
        }

        val channel = api.getTextChannelById(config.settings.noticeOfDeparture.noticeChannel)
        channel?.send("", listOf(embed))?.addActionRow(
            Button.danger("notice_of_departure_revoke", translation.buttons.textNoticeOfDepartureRevoked)
        )?.queue() ?: run {
            logger.error("Could not correctly retrieve notice of departure notice channel, does it exist?")
            return
        }
    }

    suspend fun sendRevokedMessage(reason: String, userId: String) {

    }
}