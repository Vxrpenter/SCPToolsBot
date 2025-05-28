package dev.vxrp.bot.noticeofdeparture

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.bot.noticeofdeparture.handler.NoticeOfDepartureCheckerHandler
import dev.vxrp.bot.noticeofdeparture.handler.NoticeOfDepartureMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.noticeOfDepartureScope
import dev.vxrp.util.duration.DurationParser
import dev.vxrp.util.duration.enums.DurationType
import net.dv8tion.jda.api.JDA

class NoticeOfDepartureManager(val api: JDA, val config: Config, val translation: Translation) {
    suspend fun createNotice(reason: String, handler: String, userId: String, date: String) {
        NoticeOfDepartureMessageHandler(api, config, translation).sendNoticeMessage(reason, handler, userId, date)
    }

    suspend fun revokeNotice(reason: String, userId: String, date: String) {
        NoticeOfDepartureMessageHandler(api, config, translation).sendRevokedMessage(reason, userId, NoticeOfDepartureTable().retrieveBeginDate(userId)!!, date)

        val channel = api.getTextChannelById(NoticeOfDepartureTable().retrieveChannel(userId)!!)
        val message = channel?.retrieveMessageById(NoticeOfDepartureTable().retrieveMessage(userId)!!)?.await()

        message?.delete()?.queue()

        NoticeOfDepartureTable().deleteEntry(userId)
    }

    fun spinUpChecker() {
        if (!config.settings.noticeOfDeparture.active || NoticeOfDepartureTable().retrieveSerial() == 0L) return

        Timer().runWithTimer(DurationParser().parse(
            config.settings.noticeOfDeparture.checkRate, DurationType.valueOf(config.settings.noticeOfDeparture.checkUnit)),
            noticeOfDepartureScope
        ) { NoticeOfDepartureCheckerHandler(api, config, translation).checkerTask() }
    }
}