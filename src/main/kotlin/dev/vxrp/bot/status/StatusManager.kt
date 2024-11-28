package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.bot.commands.listeners.StatusCommandListener
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.secretlab.SecretLab
import dev.vxrp.secretlab.data.Server
import dev.vxrp.secretlab.data.ServerInfo
import dev.vxrp.util.Timer
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.time.Duration.Companion.seconds

class StatusManager(val config: Config, val translation: Translation, val file: String) {
    private val logger = LoggerFactory.getLogger(StatusManager::class.java)
    private val currentFile = File("${System.getProperty("user.dir")}$file")

    private val mappedBots = hashMapOf<String, Int>()
    private val maintenance = hashMapOf<Int, Boolean>()

    init {
        if (!currentFile.exists()) {
            currentFile.createNewFile()

            val content = StatusManager::class.java.getResourceAsStream(file)
            if (content != null) currentFile.appendBytes(content.readBytes())
        }
    }

    fun initialize(commandManager: CommandManager) {
        val status = query()

        initializeBots(status, commandManager)
    }

    private val mappedServers = hashMapOf<Int, Server>()
    private fun initializeBots(status: Status, commandManager: CommandManager) {
        if (status.instances.isEmpty()) return

        for (instance in status.instances) {
            val newApi = light(instance.token, enableCoroutines = true) {
                setActivity(Activity.playing("pending..."))
            }

            mappedBots[newApi.selfUser.id] = instance.serverPort


            newApi.addEventListener(StatusCommandListener(newApi, config, translation, StatusConst(mappedBots, mappedServers, maintenance)))

            initializeCommands(commandManager, newApi)
            updateStatus(instance, status, newApi)
        }
    }

    private fun initializeCommands(commandManager: CommandManager, api: JDA) {
        commandManager.registerSpecificCommands(commandManager.query().statusCommands, api)
    }

    private fun updateStatus(instance: Instance, status: Status, api: JDA) {
        api.presence.setStatus(OnlineStatus.IDLE)

        val ports = mutableListOf<Int>()
        status.instances.forEach { currentInstance -> ports.add(currentInstance.serverPort)}

        Timer(status.cooldown.seconds).runWithTimer {
            var mappedPorts = mutableMapOf<Int, Server>()
            try {
                mappedPorts = mapPorts(status, ports)
            } catch (e: NullPointerException) {
                logger.warn("Couldn't access secretlab api, ${e.message}, retrying in ${status.cooldown} seconds")

            }
            mappedPorts[instance.serverPort]?.let { spinUpChecker(api, it, instance) }
        }
    }

    private fun mapPorts(status: Status, ports: List<Int>): MutableMap<Int, Server> {
        val secretLab = SecretLab(status.api, status.accountId)

        val map  = mutableMapOf<Int, Server>()
        for (port in ports) {
            val info = secretLab.serverInfo(lo = false, players = true, list = true)

            val server = serverByPort(port, info)

            if (server != null) {
                mappedServers[port] = server
            }

            if (info != null && server != null) map[port] = server
        }
        return map
    }

    private fun serverByPort(port: Int, info: ServerInfo?): Server? {
        for (server in info?.servers!!) {
            if (server.port == port) return server
        }
        return null
    }

    private fun spinUpChecker(api: JDA, server: Server, instance: Instance) {
        logger.debug("Updating status of bot: ${api.selfUser.name} (${api.selfUser.id}) for server - ${server.port}")
        maintenance.putIfAbsent(server.port, false)
        if (server.online) {

            if (server.players?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.get(0).equals("0")) api.presence.setStatus(OnlineStatus.IDLE)

            else if (maintenance[server.port] == false) api.presence.setStatus(OnlineStatus.ONLINE)

            if (maintenance[server.port] == true) api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)


            if (server.players != null && maintenance[server.port] == false) {
                api.presence.activity = Activity.playing(server.players)
            } else {
                api.presence.activity = Activity.customStatus("Server is in maintenance...")
            }
        } else {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            api.presence.activity = Activity.customStatus("Server Offline...")
        }

        postStatusUpdate(server, api, instance)
    }

    private val serverStatus = hashMapOf<Int, Boolean>()
    private val reconnectAttempt = hashMapOf<Int, Int>()

    private fun postStatusUpdate(server: Server, api: JDA, instance: Instance) {
        serverStatus.putIfAbsent(server.port, server.online)
        reconnectAttempt.putIfAbsent(server.port, 1)

        if (server.online) {
            if (serverStatus[server.port] == true) return

            postConnectionEstablished(api, instance)
            reconnectAttempt[server.port] = 1
            serverStatus[server.port] = true
            logger.info("Connection to server ${instance.name} (${instance.serverPort}) regained")
        } else {
            if (serverStatus[server.port] == false) return

            if (reconnectAttempt[server.port]!! == instance.retries+1) return
            if (reconnectAttempt[server.port]!! == instance.retries) {
                logger.warn("Completely lost connection to server ${instance.name} (${instance.serverPort})")

                postConnectionLost(api, instance)
                reconnectAttempt[server.port] = instance.retries+1
                serverStatus[server.port] = false
                return
            }
            logger.warn("Lost connection to server - ${instance.name} (${instance.serverPort}), trying reconnect... iteration ${reconnectAttempt[server.port]}")
            reconnectAttempt[server.port] = reconnectAttempt[server.port]!!+1
        }
    }

    private fun postConnectionEstablished(api: JDA, instance: Instance) {
        val embed = Embed {
            color = 0x2ECC70
            url = "https://status.scpslgame.com/"
            title = "${instance.name} - connection reestablished"
            description = "The connection to the server has been reestablished! Outages should not occur often when the server is running stable"
            field {
                name = "Reason"
                value = "Server is online and central servers are up"
            }
        }

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionLost(api: JDA, instance: Instance) {
        val embed = Embed {
            color = 0xE74D3C
            url = "https://status.scpslgame.com/"
            title = "${instance.name} - connection lost"
            description = "The connection to this server has been lost after **${instance.retries}** consecutive failures. Server has been marked as offline"
            field {
                name = "Reason"
                value = "Server is down, central servers are down or the secret lab api is down"
            }
        }

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    fun query(): Status {
        return Json.decodeFromString<Status>(currentFile.readText())
    }
}