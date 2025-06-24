/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.bot.commands.CommandManager
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
import dev.vxrp.util.coroutines.statusbotScope
import dev.vxrp.util.launch.LaunchOptionManager
import dev.vxrp.util.launch.enums.LaunchOptionType
import dev.vxrp.util.statusInstances
import dev.vxrp.util.statusMappedBots
import dev.vxrp.util.statusMappedServers
import io.github.vxrpenter.secretlab.SecretLab
import io.github.vxrpenter.secretlab.data.Server
import io.github.vxrpenter.secretlab.data.ServerInfo
import io.github.vxrpenter.secretlab.exceptions.CallFailureException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.time.Duration.Companion.seconds

class StatusManager(private val globalApi: JDA, val config: Config, val translation: Translation, val file: String) {
    private val logger = LoggerFactory.getLogger(StatusManager::class.java)
    private val currentFile = File(System.getProperty("user.dir")).resolve(file)
    
    private var secondsWithoutNewData = 0
    private var nonChangedData = false
    private var portToServerMap: MutableMap<Int, Server?>? = null


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

            statusMappedBots[newApi.selfUser.id] = instance.serverPort
            statusInstances[instance.serverPort] = instance

            val launchOptionManager = LaunchOptionManager(config, translation)
            if (launchOptionManager.checkLaunchOption(LaunchOptionType.STATUS_COMMAND_LISTENER).engage) StatusCommandListener(newApi, config, translation)

            if (launchOptionManager.checkLaunchOption(LaunchOptionType.COMMAND_MANAGER).engage) initializeCommands(commandManager, newApi)
            instanceApiMapping[instance] = newApi
        }

        initializeTimers(status, instanceApiMapping)
    }

    private fun initializeCommands(commandManager: CommandManager, api: JDA) {
        commandManager.registerSpecificCommands(config.extra.commands.statusCommands, api)
    }

    private fun initializeTimers(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        Timer().runWithTimer(1.seconds, statusbotScope) {
            if (nonChangedData && status.idleAfter != secondsWithoutNewData) secondsWithoutNewData += 1
        }

        Timer().runLooped(statusbotScope) {
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

        var portToServerMap: MutableMap<Int, Server?>

        val content: Pair<ServerInfo?, MutableMap<Int, Server?>>? = fetchData(status, ports)

        if (content != null) {
            portToServerMap = content.second
        } else {
            logger.error("Could not receive data for status-bots, skipping iteration")
            return
        }
        StatusConnectionHandler(translation, config).postApiConnectionUpdate(globalApi, content.first)

        if (this.portToServerMap == portToServerMap) {
            nonChangedData = true
        } else {
            secondsWithoutNewData = 0
            this.portToServerMap = portToServerMap
            nonChangedData = false
        }

        if (status.checkPlayerlist) StatusPlayerlistHandler(config, translation).updatePlayerLists(portToServerMap, status.instances, instanceApiMap)

        for (instance in status.instances) {
            val api = instanceApiMap[instance] ?: continue

            api.presence.setStatus(OnlineStatus.IDLE)

            portToServerMap[instance.serverPort]?.let { spinUpChecker(api, it, instance, content.first) }
        }
    }

    private fun fetchData(status: Status, ports: List<Int>): Pair<ServerInfo?, MutableMap<Int, Server?>>? {
        val secretLab = SecretLab(status.api, status.accountId)

        val portToServerMap = mutableMapOf<Int, Server?>()
        try {
            val info = secretLab.serverInfo(lo = false, players = true, list = true)
            for (port in ports) {
                val server = serverByPort(port, info)

                statusMappedServers[port] = server
                portToServerMap[port] = server
            }
            return Pair(info, portToServerMap)
        } catch (e: CallFailureException) {
            logger.error("Could not process secret lab request correctly ${e.cause}")
            return null
        }
    }

    private fun serverByPort(port: Int, info: ServerInfo?): Server? {
        if (info?.servers == null) return null
        for (server in info.servers!!) {
            if (server.port == port) return server
        }
        return null
    }

    private fun spinUpChecker(api: JDA, server: Server, instance: Instance, info: ServerInfo?) {
        StatusActivityHandler(translation, config).updateStatus(api, server, instance)

        StatusConnectionHandler(translation, config).postStatusUpdate(server, api, instance, info)
    }
}
