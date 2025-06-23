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