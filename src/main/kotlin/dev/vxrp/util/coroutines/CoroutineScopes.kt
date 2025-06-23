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

package dev.vxrp.util.coroutines

import dev.vxrp.bot.BotManager
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureManager
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.web.WebServerManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import org.slf4j.LoggerFactory

/**
 * CoroutineScope that is used for running periodical update checks
 */
val updatesScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(StatusManager::class.java).error("An error occurred in the update coroutine", exception)
})

/**
 * CoroutineScope that is used for running the status bots
 */
val statusbotScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(StatusManager::class.java).error("An error occurred in the statusbot coroutine", exception)
})

/**
 * CoroutineScope that is used for running periodical notice of departure checks
 */
val noticeOfDepartureScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(NoticeOfDepartureManager::class.java)
        .error("An error occurred in the notice of departure coroutine", exception)
})

/**
 * CoroutineScope that is used for running periodical regulars checks
 */
val regularsScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(RegularsManager::class.java)
        .error("An error occurred in the regulars coroutine", exception)
})

/**
 * CoroutineScope that is used for running the webserver
 */
val webServerScope = CoroutineScope(CoroutineExceptionHandler {_, exception ->
    LoggerFactory.getLogger(WebServerManager::class.java)
        .error("An error occurred in the webserver coroutine", exception)
})

/**
 * CoroutineScope that is used for running status bot pre-startup logic
 */
val defaultStatusScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(BotManager::class.java).error("An error occurred in the default status coroutine", exception)
})
