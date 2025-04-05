package dev.vxrp.util

import dev.vxrp.bot.BotManager
import dev.vxrp.bot.noticeofdeparture.NoticeOfDepartureManager
import dev.vxrp.bot.regulars.RegularsManager
import dev.vxrp.bot.status.StatusManager
import dev.vxrp.web.WebServerManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import org.slf4j.LoggerFactory

val statusbotScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(StatusManager::class.java).error("An error occurred in the statusbot coroutine", exception)
})

val noticeOfDepartureScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(NoticeOfDepartureManager::class.java)
        .error("An error occurred in the notice of departure coroutine", exception)
})

val regularsScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(RegularsManager::class.java)
        .error("An error occurred in the regulars coroutine", exception)
})

val webServerScope = CoroutineScope(CoroutineExceptionHandler {_, exception ->
    LoggerFactory.getLogger(WebServerManager::class.java)
        .error("An error occurred in the webserver coroutine", exception)
})

val defaultStatusScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    LoggerFactory.getLogger(BotManager::class.java).error("An error occurred in the default status coroutine", exception)
})
