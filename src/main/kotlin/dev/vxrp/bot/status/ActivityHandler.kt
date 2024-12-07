package dev.vxrp.bot.status

import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.secretlab.data.Server
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory

class ActivityHandler(val translation: Translation, val config: Config) {
    private val logger = LoggerFactory.getLogger(ActivityHandler::class.java)

    fun updateStatus(api: JDA, server: Server, instance: Instance, maintenance: HashMap<Int, Boolean>) {
        logger.debug("Updating status of bot: ${api.selfUser.name} (${api.selfUser.id}) for server - ${server.port}")
        maintenance.putIfAbsent(server.port, false)

        manageStatus(server, maintenance, api)
        manageActivity(server, maintenance, api)
    }

    private fun manageStatus(server: Server, maintenance: HashMap<Int, Boolean>, api: JDA) {
        if (!server.online) {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            return
        }

        if (maintenance[server.port] == true) {
            api.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
        } else {
            if (server.players?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.get(0).equals("0")) {
                api.presence.setStatus(OnlineStatus.IDLE)
                return
            }

            api.presence.setStatus(OnlineStatus.ONLINE)
        }
    }

    private fun manageActivity(server: Server, maintenance: HashMap<Int, Boolean>, api: JDA) {
        if (!server.online) {
            api.presence.activity = Activity.customStatus(translation.status.activityOffline)
            return
        }

        if  (maintenance[server.port] == true) {
            api.presence.activity = Activity.customStatus(translation.status.activityMaintenance)
        } else {
            if(server.players != null) {
                api.presence.activity = Activity.playing(server.players)
            }
        }
    }
}