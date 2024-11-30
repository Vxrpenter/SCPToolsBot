package dev.vxrp.bot.status

import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.commands.commanexecutes.status.Playerlist
import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.bot.commands.listeners.StatusCommandListener
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.bot.status.data.Status
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.StatusTable
import dev.vxrp.secretlab.SecretLab
import dev.vxrp.secretlab.data.Server
import dev.vxrp.secretlab.data.ServerInfo
import dev.vxrp.util.Timer
import dev.vxrp.util.color.ColorTool
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.MessageEmbed
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.time.Duration.Companion.seconds

class StatusManager(val config: Config, val translation: Translation, private val timer: Timer, val file: String) {
    private val logger = LoggerFactory.getLogger(StatusManager::class.java)
    private val currentFile = File(System.getProperty("user.dir")).resolve(file)

    private val mappedBots = hashMapOf<String, Int>()
    private val maintenance = hashMapOf<Int, Boolean>()

    private val mappedStatusConst = mutableMapOf<Int, StatusConst>()
    private val mappedServers = hashMapOf<Int, Server>()

    private val serverStatus = hashMapOf<Int, Boolean>()
    private val reconnectAttempt = hashMapOf<Int, Int>()

    private var secondsWithoutNewData = 0
    private var nonChangedData = false
    private var mapped: MutableMap<Int, Server>? = null


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

    private fun initializeBots(status: Status, commandManager: CommandManager) {
        if (status.instances.isEmpty()) return

        val instanceApiMapping = mutableMapOf<Instance, JDA>()

        for (instance in status.instances) {
            val newApi = light(instance.token, enableCoroutines = true) {
                setActivity(Activity.playing("pending..."))
            }

            mappedBots[newApi.selfUser.id] = instance.serverPort
            mappedStatusConst[instance.serverPort] = StatusConst(mappedBots, mappedServers, maintenance)

            if (status.initializeListeners) newApi.addEventListener(StatusCommandListener(newApi, config, translation, StatusConst(mappedBots, mappedServers, maintenance)))
            if (status.initializeCommands) initializeCommands(commandManager, newApi)
            instanceApiMapping[instance] = newApi
        }

        updateStatus(status, instanceApiMapping)
    }

    private fun initializeCommands(commandManager: CommandManager, api: JDA) {
        commandManager.registerSpecificCommands(commandManager.query().statusCommands, api)
    }

    private fun updateStatus(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
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
            task(status, instanceApiMap)
            delay(status.idleCheckRate.seconds)
        } else {
            task(status, instanceApiMap)
            delay(status.checkRate.seconds)
        }
    }

    private fun task(status: Status, instanceApiMap: MutableMap<Instance, JDA>) {
        val ports = mutableListOf<Int>()
        status.instances.forEach { currentInstance -> ports.add(currentInstance.serverPort)}

        var mappedPorts = mutableMapOf<Int, Server>()
        try {
            mappedPorts = mapPorts(status, ports)
        } catch (e: NullPointerException) {
            logger.warn("Couldn't access secretlab api, ${e.message}, retrying in ${status.checkRate} seconds")
        }

        if (mapped == mappedPorts) {
            nonChangedData = true
        } else {
            secondsWithoutNewData = 0
            mapped = mappedPorts
            nonChangedData = false
        }

        if (status.checkPlayerlist) updatePlayerLists(mappedPorts, status.instances, instanceApiMap)

        for (instance in status.instances) {
            val api = instanceApiMap[instance] ?: continue

            api.presence.setStatus(OnlineStatus.IDLE)

            mappedPorts[instance.serverPort]?.let { spinUpChecker(api, it, instance) }
        }
    }

    private fun mapPorts(status: Status, ports: List<Int>): MutableMap<Int, Server> {
        val secretLab = SecretLab(status.api, status.accountId)

        val map  = mutableMapOf<Int, Server>()
        val info = secretLab.serverInfo(lo = false, players = true, list = true)

        for (port in ports) {

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
                api.presence.activity = Activity.customStatus(translation.status.activityOffline)
            }
        } else {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            api.presence.activity = Activity.customStatus(translation.status.activityMaintenance)
        }

        postStatusUpdate(server, api, instance)
    }

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

    private fun updatePlayerLists(ports: MutableMap<Int, Server>, instances: List<Instance>, instanceApiMap: MutableMap<Instance, JDA>) {
        for (port in ports) {
            var api: JDA? = null

            for (instance in instances) {
                if (instance.serverPort == port.key) {
                    api = instanceApiMap[instance]
                    break
                }
            }

            transaction {
                StatusTable.Status.select(StatusTable.Status.channelId, StatusTable.Status.messageId)
                    .where { StatusTable.Status.port.eq(port.key.toString()) }
                    .forEach {
                        if (api == null) return@transaction
                        val embeds = mutableListOf<MessageEmbed>()
                        mappedStatusConst[port.key]?.let { statusConst ->
                            Playerlist().getEmbed(api.selfUser.id, translation, statusConst)
                        }?.let { playerListEmbed ->
                            embeds.add(playerListEmbed)
                        }

                        api.getTextChannelById(it[StatusTable.Status.channelId])
                            ?.editMessage(it[StatusTable.Status.messageId], null, embeds)?.queue()

                        logger.debug("Updated playerlist with message id: ${it[StatusTable.Status.messageId]} in channel ${it[StatusTable.Status.channelId]} part of server ${port.key}")
                    }
            }

        }
    }

    private fun postConnectionEstablished(api: JDA, instance: Instance) {
        val embed = Embed {
            color = 0x2ECC70
            url = "https://status.scpslgame.com/"
            title = ColorTool().useCustomColorCodes(translation.status.embedEstablishedTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedEstablishedBody).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedFieldValue).trimIndent()
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
            title = ColorTool().useCustomColorCodes(translation.status.embedLostTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedLostBody
                .replace("%retries%", instance.retries.toString())).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedLostFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedLostFieldValue).trimIndent()
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