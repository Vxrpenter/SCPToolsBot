package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
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

class StatusManager(val config: Config, val file: String) {
    private val logger = LoggerFactory.getLogger(StatusManager::class.java)
    private val currentFile = File("${System.getProperty("user.dir")}$file")

    init {
        if (!currentFile.exists()) {
            currentFile.createNewFile()

            val content = StatusManager::class.java.getResourceAsStream(file)
            if (content != null) currentFile.appendBytes(content.readBytes())
        }
    }

    fun initialize(commandManager: CommandManager) {
        val status = Json.decodeFromString<Status>(currentFile.readText())
        if (!status.active) return

        initializeBots(status, commandManager)
    }

    private fun initializeBots(status: Status, commandManager: CommandManager) {
        if (status.instances.isEmpty()) return

        for (instance in status.instances) {
            val newApi = light(instance.token, enableCoroutines = false) {
                setActivity(Activity.playing("pending..."))
            }

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
            mappedPorts[instance.serverPort]?.let { spinUpChecker(api, it) }
        }
    }

    private fun mapPorts(status: Status, ports: List<Int>): MutableMap<Int, Server> {
        val secretLab = SecretLab(status.api, status.accountId)

        val map  = mutableMapOf<Int, Server>()
        for (port in ports) {
            val info = secretLab.serverInfo(lo = false, players = true)

            val server = serverByPort(port, info)

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

    var maintenance = false

    private fun spinUpChecker(api: JDA, server: Server) {
        logger.debug("Updating status of bot: ${api.selfUser.name} (${api.selfUser.id}) for server - ${server.port}")
        if (server.online) {

            if (server.players?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.get(0).equals("0")) api.presence.setStatus(OnlineStatus.IDLE)

            else if (!maintenance) api.presence.setStatus(OnlineStatus.ONLINE)

            if (maintenance) api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)


            if (server.players != null && !maintenance) {
                api.presence.activity = Activity.playing(server.players)
            } else {
                api.presence.activity = Activity.customStatus("Server is in maintenance...")
            }
        } else {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            api.presence.activity = Activity.customStatus("Server Offline...")
        }

        postStatusUpdate(server, api)
    }

    private val serverStatus = mutableMapOf<Server, Boolean>()
    private val reconnectAttempt = mutableMapOf<Server, Int>()

    private fun postStatusUpdate(server: Server, api: JDA) {
        if (serverStatus[server] == null) serverStatus[server] = server.online
        if (reconnectAttempt[server] == null) reconnectAttempt[server] = 0

        if (server.online) {
            if (serverStatus[server] == true) return

            postConnectionEstablished(api)
            reconnectAttempt[server] = 0
            logger.info("Connection to server ${server.port} regained")
        } else {
            if (serverStatus[server] == false) return

            if (reconnectAttempt[server]!! >= 4) {
                logger.warn("Completely lost connection to server ${server.port} - ")

                postConnectionLost(api)
                return
            }
            logger.warn("Lost connection to server - ${server.port}, trying reconnect... iteration ${reconnectAttempt[server]}")
            reconnectAttempt[server] = reconnectAttempt[server]!!+1
        }
    }

    private fun postConnectionEstablished(api: JDA) {
        val embed = Embed {
            color = 0x2ECC70
            url = "https://status.scpslgame.com/"
            title = "Connection to server reestablished"
            description = "The connection to this server has been reestablished. Lost connection could be the cause of network issues or an offline server"
            field {
                name = "Reason"
                value = "Server is online and central servers are up"
            }
        }

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }

    private fun postConnectionLost(api: JDA) {
        val embed = Embed {
            color = 0xE74D3C
            url = "https://status.scpslgame.com/"
            title = "Connection to server lost"
            description = "The connection to this server has been lost. Lost connection could be the cause of network issues or an offline server. " +
                    "Tell your system administrator so they can resolve these issues"
            field {
                name = "Reason"
                value = "Server is down, central servers are down or the secret lab api is down"
            }
        }

        if (config.status.postServerStatus) {
            api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
        }
    }
}