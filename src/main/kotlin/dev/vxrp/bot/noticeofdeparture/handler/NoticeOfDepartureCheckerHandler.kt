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

package dev.vxrp.bot.noticeofdeparture.handler

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoticeOfDepartureCheckerHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(NoticeOfDepartureCheckerHandler::class.java)

    suspend fun checkerTask() {
        val notices = NoticeOfDepartureTable().retrieveSerial()
        logger.info("Starting notice of departure checker, processing units starting...")
        val idList = NoticeOfDepartureTable().retrieveAllIds()
        var redundantNotices = 0

        for (id in idList) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern(config.settings.noticeOfDeparture.dateFormatting)
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

        logger.info("$redundantNotices out of $notices notices were found to be redundant, checker ending process...")
    }
}