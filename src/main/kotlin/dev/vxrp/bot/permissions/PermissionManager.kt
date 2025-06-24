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

package dev.vxrp.bot.permissions

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.bot.permissions.handler.PermissionMessageHandler
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
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

        val message = PermissionMessageHandler(config, translation).getPermissionMessage(permissionType)
        logger.debug("Permission action for user: {}, for permission type: {}, denied", user.id, permissionType)
        return Pair(false, message)
    }

    fun checkStatus(messageType: StatusMessageType, vararg checks: Boolean): MessageEmbed? {
        for (check in checks) {
            if (!check) {
                return PermissionMessageHandler(config, translation).getStatusMessage(messageType)
            }
            continue
        }
        return null
    }

    private suspend fun queryUserRoles(user: User): MutableList<String>? {
        val roleIdList = mutableListOf<String>()
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

            PermissionType.NOTICE_OF_DEPARTURES -> {
                if (config.settings.noticeOfDeparture.rolesAccess.isEmpty()) {
                    logger.error("Could not find any roles to determine permissions for notice of departures")
                    return null
                }
                return config.settings.noticeOfDeparture.rolesAccess
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