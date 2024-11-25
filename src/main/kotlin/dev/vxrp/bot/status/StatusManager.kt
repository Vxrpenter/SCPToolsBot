package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.status.data.Status
import dev.vxrp.secretlab.SecretLab
import dev.vxrp.secretlab.data.ServerInfo
import dev.vxrp.util.Timer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import java.io.File
import java.sql.Time
import java.time.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StatusManager(val file: String) {
    private val currentFile = File("${System.getProperty("user.dir")}$file")

    init {
        if (!currentFile.exists()) {
            currentFile.createNewFile()

            val content = StatusManager::class.java.getResourceAsStream(file)
            if (content != null) currentFile.appendBytes(content.readBytes())
        }
    }

    fun initialize() {
        val status = Json.decodeFromString<Status>(currentFile.readText())
        if (!status.active) return

        initializeBots(status)
    }

    private fun initializeBots(status: Status) {
        val bots = mutableListOf<JDA>()

        for (instance in status.instances) {
            val newApi = light(instance.token, enableCoroutines = false) {
                setActivity(Activity.playing("pending..."))
            }
            bots.add(newApi)

            val ports = mutableListOf<Int>()
            status.instances.forEach { currentInstance -> ports.add(currentInstance.serverPort)}

            val mappedPorts = mapPorts(status.api, status.accountId, ports)


        }
    }

    private fun mapPorts(apiKey: String, accountId: String, ports: List<Int>): Map<Int, ServerInfo> {
        val secretLab = SecretLab(apiKey, accountId)

        val map  = mutableMapOf<Int, ServerInfo>()
        for (port in ports) {
            val info = secretLab.serverInfo(lo = false, players = true)
            println(info)

            if (info != null) map[port] = info
        }
        return map
    }

    private fun spinUpTaskTimers(api: JDA, ports: Map<Int, ServerInfo>) {
        Timer(13.seconds).runWithTimer {

        }
    }
}