package dev.vxrp.bot.noticeofdeparture

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import dev.vxrp.util.Timer
import dev.vxrp.util.duration.DurationParser
import dev.vxrp.util.duration.enums.DurationType
import dev.vxrp.util.noticeOfDepartureScope
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoticeOfDepartureManager(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(NoticeOfDepartureManager::class.java)

    suspend fun createNotice(reason: String, handler: String, userId: String, date: String) {
        NoticeOfDepartureMessageHandler(api, config, translation).sendNoticeMessage(reason, handler, userId, date)
    }

    suspend fun revokeNotice(reason: String, userId: String, date: String) {
        NoticeOfDepartureMessageHandler(api, config, translation).sendRevokedMessage(reason, userId, NoticeOfDepartureTable().retrieveBeginDate(userId)!!, date)

        NoticeOfDepartureTable().deleteEntry(userId)
    }

    fun spinUpChecker() {
        if (!config.settings.noticeOfDeparture.active) return

        Timer().runWithTimer(DurationParser().parse(
            config.settings.noticeOfDeparture.checkRate,
            DurationType.valueOf(config.settings.noticeOfDeparture.checkUnit)),
            noticeOfDepartureScope
        ) { checkerTask() }
    }

    private suspend fun checkerTask() {
        logger.info("Starting notice of departure checker, processing units starting...")
        val idList = NoticeOfDepartureTable().retrieveAllIds()
        var redundantNotices = 0

        for (id in idList) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val beginDate = LocalDate.parse(NoticeOfDepartureTable().retrieveBeginDate(id)!!, formatter)
            val endDate = LocalDate.parse(NoticeOfDepartureTable().retrieveEndDate(id)!!, formatter)

            val daysUntil = currentDate.until(endDate).days

            if (daysUntil == 0 || daysUntil < 0) {
                redundantNotices += 1
                logger.info("Found redundant notice of departure, ending notice and processing database data")
                NoticeOfDepartureMessageHandler(api, config, translation).sendEndedMessage(id, beginDate.format(formatter), endDate.format(formatter))
                NoticeOfDepartureTable().deleteEntry(id)
                logger.info("Deleted notice of departure - $id successfully")
            }
        }

        logger.info("$redundantNotices out of ${NoticeOfDepartureTable().retrieveSerial()} notices were found to be redundant, checker ending process...")
    }
}