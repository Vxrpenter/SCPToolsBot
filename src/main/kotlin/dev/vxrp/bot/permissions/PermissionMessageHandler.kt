package dev.vxrp.bot.permissions

import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PermissionMessageHandler(val config: Config, val translation: Translation) {
    private val logger: Logger = LoggerFactory.getLogger(PermissionMessageHandler::class.java)

    fun sendSpecifiedMessage(permissionType: PermissionType, permitted: Boolean) {
        when (permissionType) {
            PermissionType.TICKET -> {

            }

            PermissionType.TICKET_LOGS -> {

            }

            PermissionType.REGULARS -> {

            }

            PermissionType.STATUS_BOT -> {

            }

            PermissionType.APPLICATION -> {

            }
        }
    }
}