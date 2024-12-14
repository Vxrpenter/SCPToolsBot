package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.bot.commands.listeners.StatusCommandListener
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.api.sla.secretlab.SecretLab
import dev.vxrp.secretlab.data.Server
import dev.vxrp.secretlab.data.ServerInfo
import dev.vxrp.util.Timer
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.time.Duration.Companion.seconds

class StatusManager(val globalApi: JDA, val config: Config, val translation: Translation, private val timer: Timer, val file: String) {
    private val logger = LoggerFactory.getLogger(StatusManager::class.java)
    private val currentFile = File(System.getProperty("user.dir")).resolve(file)

    private val mappedBots = hashMapOf<String, Int>()
    private val maintenance = hashMapOf<Int, Boolean>()

    private val retryFetchData = 0

    private val mappedStatusConstructor = mutableMapOf<Int, StatusConstructor>()
    private val mappedServers = hashMapOf<Int, Server>()
    
    private var secondsWithoutNewData = 0
    private var nonChangedData = false
    private var mapped: MutableMap<Int, Server>? = null


    init {
        if (!currentFile.exists()) {
            currentFile.createNewFile()

            val content = StatusManager::class.java.getResourceAsStream("/$file")
            if (content != null) currentFile.appendBytes(content.readBytes())
        }
    }

    fun initialize(commandManager: CommandManager) {
        maintenance.clear()
        mappedBots.clear()
        mappedStatusConstructor.clear()
        mappedServers.clear()

        val status = query()

        initializeBots(status, commandManager)
    }

    private fun initializeBots(status: Status, commandManager: CommandManager) {
        if (status.instances.isEmpty()) return

        val instanceApiMapping = mutableMapOf<Instance, JDA>()

        for (instance in status.instances) {
            val newApi = light(instance.token, enableCoroutines = true) {
                setActivity(Activity.playing("pending..."))
            }

            mappedBots[newApi.selfUser.id] = instance.serverPort
            mappedStatusConstructor[instance.serverPort] = StatusConstructor(mappedBots, mappedServers, maintenance, instance)

            if (status.initializeListeners) newApi.addEventListener(
                StatusCommandListener(newApi, config, translation, StatusConstructor(mappedBots, mappedServers, maintenance, instance))
            )
            if (status.initializeCommands) initializeCommands(commandManager, newApi)
            instanceApiMapping[instance] = newApi
        }

        initializeTimers(status, instanceApiMapping)
    }

    private fun initializeCommands(commandManager: CommandManager, api: JDA) {
        commandManager.registerSpecificCommands(commandManager.query().statusCommands, api)
    }

    private fun initializeTimers(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        timer.runWithTimer(1.seconds) {
            if (nonChangedData && status.idleAfter != secondsWithoutNewData) secondsWithoutNewData += 1
        }

        timer.runLooped {
            runTimer(status, instanceApiMap)
        }
    }

    private suspend fun runTimer(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        if (secondsWithoutNewData == status.idleAfter) {
            logger.debug("Data hasn't changed for the last ${status.idleAfter} seconds, using check rate of ${status.idleCheckRate} seconds")
            runStatusChange(status, instanceApiMap)
            delay(status.idleCheckRate.seconds)
        } else {
            runStatusChange(status, instanceApiMap)
            delay(status.checkRate.seconds)
        }
    }

    private fun runStatusChange(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        val ports = mutableListOf<Int>()
        status.instances.forEach { currentInstance -> ports.add(currentInstance.serverPort) }

        var mappedPorts = mutableMapOf<Int, Server>()

        val content: Pair<ServerInfo?, MutableMap<Int, Server>>? = fetchData(status, ports)

        // Check if data was received
        if (content != null) {
            mappedPorts = content.second
        }
        ConnectionHandler(translation, config).postApiConnectionUpdate(globalApi, status, content)

        if (mapped == mappedPorts) {
            nonChangedData = true
        } else {
            secondsWithoutNewData = 0
            mapped = mappedPorts
            nonChangedData = false
        }

        if (status.checkPlayerlist) PlayerlistHandler(translation).updatePlayerLists(mappedPorts, status.instances, instanceApiMap, mappedStatusConstructor)

        for (instance in status.instances) {
            val api = instanceApiMap[instance] ?: continue

            api.presence.setStatus(OnlineStatus.IDLE)

            mappedPorts[instance.serverPort]?.let { spinUpChecker(api, it, instance, content?.first) }
        }
    }

    private fun fetchData(status: Status, ports: List<Int>): Pair<ServerInfo?, MutableMap<Int, Server>>? {
        val secretLab = SecretLab(status.api, status.accountId)

        val map = mutableMapOf<Int, Server>()
        val info = secretLab.serverInfo(lo = false, players = true, list = true) ?: return null

        for (port in ports) {
            val server = serverByPort(port, info) ?: return null

            mappedServers[port] = server
            map[port] = server
        }
        return Pair(info, map)
    }

    private fun serverByPort(port: Int, info: ServerInfo?): Server? {
        for (server in info?.servers!!) {
            if (server.port == port) return server
        }
        return null
    }

    private fun spinUpChecker(api: JDA, server: Server, instance: Instance, info: ServerInfo?) {
        ActivityHandler(translation, config).updateStatus(api, server, instance, maintenance)

        ConnectionHandler(translation, config).postStatusUpdate(server, api, instance, info)
    }

    fun query(): Status {
        return Json.decodeFromString<Status>(currentFile.readText())
    }
}