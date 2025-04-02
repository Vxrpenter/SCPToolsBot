package dev.vxrp.bot.noticeofdeparture

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.NoticeOfDepartureTable
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory

class NoticeOfDepartureManager(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(NoticeOfDepartureManager::class.java)

    suspend fun createNotice(reason: String, handler: String, userId: String, date: String) {
        NoticeOfDepartureMessageHandler(api, config, translation).sendNoticeMessage(reason, handler, userId, date)
    }

    suspend fun revokeNotice(reason: String, userId: String, date: String) {
        NoticeOfDepartureMessageHandler(api, config, translation).sendRevokedMessage(reason, userId, date)

        NoticeOfDepartureTable().deleteEntry(userId)
    }
}