package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import io.github.vxrpenter.secretlab.SecretLab
import io.github.vxrpenter.secretlab.data.Server
import io.github.vxrpenter.secretlab.data.ServerInfo
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.bot.commands.listeners.StatusCommandListener
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.bot.status.handler.StatusActivityHandler
import dev.vxrp.bot.status.handler.StatusConnectionHandler
import dev.vxrp.bot.status.handler.StatusPlayerlistHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.ConnectionTable
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.defaultStatusScope
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionType
import dev.vxrp.util.coroutines.statusbotScope
import io.github.vxrpenter.secretlab.exceptions.CallFailureException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.time.Duration.Companion.seconds

val bots = mutableListOf<JDA>()

class StatusManager(private val globalApi: JDA, val config: Config, val translation: Translation, private val timer: Timer, val file: String) {
    private val logger = LoggerFactory.getLogger(StatusManager::class.java)
    private val currentFile = File(System.getProperty("user.dir")).resolve(file)

    private val mappedBots = hashMapOf<String, Int>()

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
        if (!config.status.active) return

        defaultStatusScope.launch {
            mappedBots.clear()
            mappedStatusConstructor.clear()
            mappedServers.clear()

            val status = config.status

            initializeBots(status, commandManager)
        }
    }

    private fun initializeBots(status: Status, commandManager: CommandManager) {
        if (status.instances.isEmpty()) return

        val instanceApiMapping = mutableMapOf<Instance, JDA>()

        for (instance in status.instances) {
            ConnectionTable().insertIfNotExists(instance.serverPort.toString(), status = true, maintenance = false)

            val newApi = light(instance.token, enableCoroutines = true) {
                setActivity(Activity.playing("pending..."))
            }
            logger.info("Starting up status-bot: ${newApi.awaitReady().selfUser.id}")
            bots.add(newApi)

            mappedBots[newApi.selfUser.id] = instance.serverPort
            mappedStatusConstructor[instance.serverPort] = StatusConstructor(mappedBots, mappedServers, instance)

            val launchOptionManager = LaunchOptionManager(config, translation)
            if (launchOptionManager.checkLaunchOption(LaunchOptionType.STATUS_COMMAND_LISTENER).engage) StatusCommandListener(newApi, config, translation, StatusConstructor(mappedBots, mappedServers, instance))

            if (launchOptionManager.checkLaunchOption(LaunchOptionType.COMMAND_MANAGER).engage) initializeCommands(commandManager, newApi)
            instanceApiMapping[instance] = newApi
        }

        initializeTimers(status, instanceApiMapping)
    }

    private fun initializeCommands(commandManager: CommandManager, api: JDA) {
        commandManager.registerSpecificCommands(config.extra.commands.statusCommands, api)
    }

    private fun initializeTimers(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        timer.runWithTimer(1.seconds, statusbotScope) {
            if (nonChangedData && status.idleAfter != secondsWithoutNewData) secondsWithoutNewData += 1
        }

        timer.runLooped(statusbotScope) {
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

    private suspend fun runStatusChange(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        val ports = mutableListOf<Int>()
        status.instances.forEach { currentInstance -> ports.add(currentInstance.serverPort) }

        var mappedPorts: MutableMap<Int, Server>

        val content: Pair<ServerInfo?, MutableMap<Int, Server>>? = fetchData(status, ports)

        // Check if data was received
        if (content != null) {
            mappedPorts = content.second
        } else {
            logger.error("Could not receive data for status-bots, skipping iteration")
            return
        }
        StatusConnectionHandler(translation, config).postApiConnectionUpdate(globalApi, status, content)

        if (mapped == mappedPorts) {
            nonChangedData = true
        } else {
            secondsWithoutNewData = 0
            mapped = mappedPorts
            nonChangedData = false
        }

        if (status.checkPlayerlist) StatusPlayerlistHandler(config, translation).updatePlayerLists(mappedPorts, status.instances, instanceApiMap, mappedStatusConstructor)

        for (instance in status.instances) {
            val api = instanceApiMap[instance] ?: continue

            api.presence.setStatus(OnlineStatus.IDLE)

            mappedPorts[instance.serverPort]?.let { spinUpChecker(api, it, instance, content.first) }
        }
    }

    private fun fetchData(status: Status, ports: List<Int>): Pair<ServerInfo?, MutableMap<Int, Server>>? {
        val secretLab = SecretLab(status.api, status.accountId)

        val map = mutableMapOf<Int, Server>()
        try {
            val info = secretLab.serverInfo(lo = false, players = true, list = true)
            for (port in ports) {
                val server = serverByPort(port, info) ?: return null

                mappedServers[port] = server
                map[port] = server
            }
            return Pair(info, map)
        } catch (e: CallFailureException) {
            logger.error("Could not process secret lab request correctly ${e.message}")
            return null
        }
    }

    private fun serverByPort(port: Int, info: ServerInfo?): Server? {
        for (server in info?.servers!!) {
            if (server.port == port) return server
        }
        return null
    }

    private fun spinUpChecker(api: JDA, server: Server, instance: Instance, info: ServerInfo?) {
        StatusActivityHandler(translation, config).updateStatus(api, server, instance)

        StatusConnectionHandler(translation, config).postStatusUpdate(server, api, instance, info)
    }
}
