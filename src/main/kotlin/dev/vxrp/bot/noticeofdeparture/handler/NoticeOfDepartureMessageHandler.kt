package dev.vxrp.bot.noticeofdeparture.handler

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.TimeFormat
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NoticeOfDepartureMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(NoticeOfDepartureMessageHandler::class.java)

    fun sendTemplate(channel: TextChannel) {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedTemplateBody
                .replace("%formatter%", config.settings.noticeOfDeparture.dateFormatting))
        }

        channel.send("", listOf(embed)).setActionRow(
            Button.success("notice_of_departure_file", translation.buttons.textNoticeOfDepartureFile).withEmoji(Emoji.fromFormatted("⏰"))
        ).queue()
    }

    suspend fun sendDecisionMessage(userId: String, date: String, reason: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.now()
        val endDate = LocalDate.parse(date, formatter)

        val discordCurrentDate = TimeFormat.DATE_LONG.atInstant(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val discordEndDate = TimeFormat.DATE_LONG.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val relativeTime = TimeFormat.RELATIVE.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()

        val user = api.retrieveUserById(userId).await()

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedDecisionTitle
                    .replace("%number%", (NoticeOfDepartureTable().retrieveSerial() + 1).toString())
                    .replace("%user%", user.globalName.toString())
            )
            description = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedDecisionBody
                    .replace("%current_date%", discordCurrentDate)
                    .replace("%end_date%", discordEndDate)
                    .replace("%relative%", relativeTime)
                    .replace("%reason%", reason)
            )
            timestamp = Instant.now()
        }

        val channel: TextChannel? = api.getTextChannelById(config.settings.noticeOfDeparture.decisionChannel)
        channel?.send("", listOf(embed))?.addActionRow(
            Button.success("notice_of_departure_decision_accept:$userId:${endDate.format(formatter)}", translation.buttons.textNoticeOfDepartureAccept).withEmoji(
                Emoji.fromFormatted("📩")),
            Button.danger("notice_of_departure_decision_dismiss:$userId:${endDate.format(formatter)}", translation.buttons.textNoticeOfDepartureDismissed).withEmoji(
                Emoji.fromFormatted("🫷"))
        )?.queue() ?: run{
            logger.error("Could not correctly retrieve notice of departure decision channel, does it exist?")
            return
        }
    }

    suspend fun sendAcceptedMessage(reason: String, userId: String, date: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.now()
        val endDate = LocalDate.parse(date, formatter)

        val discordCurrentDate = TimeFormat.DATE_LONG.atInstant(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val discordEndDate = TimeFormat.DATE_LONG.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val relativeTime = TimeFormat.RELATIVE.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()

        val embed = Embed {
            color = 0x2ECC70
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedAcceptedTitle)
            description = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedAcceptedBody
                    .replace("%current_date%", discordCurrentDate)
                    .replace("%end_date%", discordEndDate)
                    .replace("%relative%", relativeTime)
                    .replace("%reason%", reason)
            )
        }

        val privateChannel = api.retrieveUserById(userId).await().openPrivateChannel().await()
        privateChannel.send("", listOf(embed)).queue()
    }

    suspend fun sendDismissedMessage(reason: String, userId: String) {
        val embed = Embed {
            color = 0xE74D3C
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedDismissedTitle)
            description = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedDismissedBody
                    .replace("%reason%", reason)
            )
        }

        val privateChannel = api.retrieveUserById(userId).await().openPrivateChannel().await()
        privateChannel.send("", listOf(embed)).queue()
    }

    suspend fun sendNoticeMessage(reason: String, handlerId: String, userId: String, date: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.now()
        val endDate = LocalDate.parse(date, formatter)

        val discordCurrentDate = TimeFormat.DATE_LONG.atInstant(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val discordEndDate = TimeFormat.DATE_LONG.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val relativeTime = TimeFormat.RELATIVE.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()

        val handler = api.retrieveUserById(handlerId).await()
        val user = api.retrieveUserById(userId).await()

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedNoticeTitle
                    .replace("%number%", (NoticeOfDepartureTable().retrieveSerial() + 1).toString())
                    .replace("%user%", user.globalName.toString())
            )
            description = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedNoticeBody
                    .replace("%user%", handler.asMention)
                    .replace("%current_date%", discordCurrentDate)
                    .replace("%end_date%", discordEndDate)
                    .replace("%relative%", relativeTime)
                    .replace("%reason%", reason)
            )
        }

        val channel = api.getTextChannelById(config.settings.noticeOfDeparture.noticeChannel)
        val message = channel?.send("", listOf(embed))?.addActionRow(
            Button.danger("notice_of_departure_revoke:$userId:${date.format(formatter)}", translation.buttons.textNoticeOfDepartureRevoked)
        )?.await() ?: run {
            logger.error("Could not correctly retrieve notice of departure notice channel, does it exist?")
            return
        }

        NoticeOfDepartureTable().addToDatabase(userId, true, handlerId, channel.id, message.id, currentDate.format(formatter), endDate.format(formatter))
    }

    suspend fun sendRevokedMessage(reason: String, userId: String, beginDate: String, endDate: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.parse(beginDate, formatter)
        val endDate = LocalDate.parse(endDate, formatter)

        val discordCurrentDate = TimeFormat.DATE_LONG.atInstant(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val discordEndDate = TimeFormat.DATE_LONG.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()

        val embed = Embed {
            color = 0xE74D3C
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedRevokedTitle)
            description = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedRevokedBody
                    .replace("%current_date%", discordCurrentDate)
                    .replace("%end_date%", discordEndDate)
                    .replace("%reason%", reason)
            )
        }

        val privateChannel = api.retrieveUserById(userId).await().openPrivateChannel().await()
        privateChannel.send("", listOf(embed)).queue()
    }

    suspend fun sendEndedMessage(userId: String, beginDate: String, endDate: String) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentDate = LocalDate.parse(beginDate, formatter)
        val endDate = LocalDate.parse(endDate, formatter)

        val discordCurrentDate = TimeFormat.DATE_LONG.atInstant(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()
        val discordEndDate = TimeFormat.DATE_LONG.atInstant(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString()

        val embed = Embed {
            color = 0xE74D3C
            title = ColorTool().useCustomColorCodes(translation.noticeOfDeparture.embedEndedTitle)
            description = ColorTool().useCustomColorCodes(
                translation.noticeOfDeparture.embedEndedBody
                    .replace("%current_date%", discordCurrentDate)
                    .replace("%end_date%", discordEndDate)
            )
        }

        val privateChannel = api.awaitReady().retrieveUserById(userId).await().openPrivateChannel().await()
        privateChannel.send("", listOf(embed)).queue()

        val channel = api.awaitReady().getTextChannelById(NoticeOfDepartureTable().retrieveChannel(userId)!!)
        channel?.retrieveMessageById(NoticeOfDepartureTable().retrieveMessage(userId)!!)?.await()?.delete()?.await()
    }
}