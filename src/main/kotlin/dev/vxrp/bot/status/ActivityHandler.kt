package dev.vxrp.bot.status

import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.ConnectionTable
import dev.vxrp.secretlab.data.Server
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class ActivityHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(ActivityHandler::class.java)

    fun updateStatus(api: JDA, server: Server, instance: Instance) {
        logger.debug("Updating status of bot: ${api.selfUser.name} (${api.selfUser.id}) for server - ${server.port}")
        var currentMaintenance = false

        transaction {
            ConnectionTable.Connections.selectAll()
                .where { ConnectionTable.Connections.id eq instance.serverPort.toString()}
                .forEach {
                    currentMaintenance = it[ConnectionTable.Connections.maintenance] == true
                }
        }


        manageStatus(server, currentMaintenance, api)
        manageActivity(server, currentMaintenance, api)
    }

    private fun manageStatus(server: Server, maintenance: Boolean, api: JDA) {
        if (maintenance) {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            return
        }

        if (!server.online) {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            return
        }

        if (server.players?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.get(0).equals("0")) {
            api.presence.setStatus(OnlineStatus.IDLE)
            return
        }

        api.presence.setStatus(OnlineStatus.ONLINE)
    }

    private fun manageActivity(server: Server, maintenance: Boolean, api: JDA) {
        if  (maintenance) {
            api.presence.activity = Activity.customStatus(translation.status.activityMaintenance)
            return
        }

        if (!server.online) {
            api.presence.activity = Activity.customStatus(translation.status.activityOffline)
            return
        }

        if(server.players != null) {
            api.presence.activity = Activity.playing(server.players)
        }
    }
}