package dev.vxrp.bot.permissions

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PermissionManager(val config: Config, val translation: Translation) {
    private val logger: Logger = LoggerFactory.getLogger(PermissionManager::class.java)
    private val insufficientValuesMessage = "Please supply the needed values for permission check"

    suspend fun determinePermissions(user: User, permissionType: PermissionType, ticketType: TicketType? = null): Pair<Boolean, MessageEmbed?> {
        val rolePair: Pair<List<String>, List<String>>? = roleValidation(user, permissionType, ticketType)

        if (rolePair == null) {
            logger.warn("Permission denied due to failures in permission chain")
        }

        if (permissionType == PermissionType.TICKET && ticketType == null || permissionType == PermissionType.TICKET_LOGS && ticketType == null) {
            logger.error(this.insufficientValuesMessage)
            return Pair(false, null)
        }

        for (role in rolePair!!.first) {
            if (rolePair.second.contains(role)) {
                logger.debug("Permission action for user: {}, for permission type: {}, permitted", user.id, permissionType)
                return Pair(true, null)
            }
        }

        val message = PermissionMessageHandler(config, translation).getSpecificMessage(permissionType)
        logger.debug("Permission action for user: {}, for permission type: {}, denied", user.id, permissionType)
        return Pair(false, message)
    }

    private suspend fun queryUserRoles(user: User): MutableList<String>? {
        val roleIdList = mutableListOf<String>()
        println()
        val currentRoles = user.jda.getGuildById(config.settings.guildId)!!.retrieveMemberById(user.id).await()?.roles ?: run {
            logger.error("Could not find any roles to determine permissions for User: {}", user.id)
            return null
        }

        for (role in currentRoles) {
            roleIdList.add(role.id)
        }

        return roleIdList
    }

    private fun permissionRoles(permissionType: PermissionType, ticketType: TicketType? = null): List<String>?{
        when (permissionType) {
            PermissionType.TICKET -> {
                for (type in config.ticket.types) {
                    if (type.type.replace("support::", "") != ticketType.toString()) continue

                    if (type.roles.isEmpty()) {
                        logger.error("Could not find any roles to determine permissions for ticket type: {}", ticketType)
                        return null
                    }
                    return type.roles
                }
            }

            PermissionType.TICKET_LOGS -> {
                for (type in config.ticket.types) {
                    if (type.type.replace("support::", "") != ticketType.toString()) continue

                    if (type.logPermissionRoles.isEmpty()) {
                        logger.error("Could not find any roles to determine permissions for log of ticket type: {}", ticketType)
                        return null
                    }
                    return type.logPermissionRoles
                }
            }

            PermissionType.REGULARS -> {

            }

            PermissionType.STATUS_BOT -> {

            }

            PermissionType.APPLICATION -> {

            }
        }

        return null
    }

    private suspend fun roleValidation(user: User, permissionType: PermissionType, ticketType: TicketType? = null): Pair<List<String>, List<String>>? {
        val userRoles = queryUserRoles(user) ?: run {
            logger.error("Could not validate user roles")
            return null
        }

        val permissionRoles = permissionRoles(permissionType, ticketType) ?: run {
            logger.error("Could not validate permission roles")
            return null
        }

        return Pair(userRoles, permissionRoles)
    }
}