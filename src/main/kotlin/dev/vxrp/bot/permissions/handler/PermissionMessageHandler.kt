package dev.vxrp.bot.permissions.handler

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.permissions.enums.PermissionMessageType
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.MessageEmbed

class PermissionMessageHandler(val config: Config, val translation: Translation) {
    fun getSpecificMessage(permissionType: PermissionType): MessageEmbed? {
        when (permissionType) {
            PermissionType.TICKET -> {
                return ticketMessage(PermissionMessageType.INSUFFICIENT_PERMISSION)
            }

            PermissionType.TICKET_LOGS -> {
                return ticketMessage(PermissionMessageType.INSUFFICIENT_PERMISSION)
            }

            PermissionType.REGULARS -> {

            }

            PermissionType.STATUS_BOT -> {

            }

            PermissionType.NOTICE_OF_DEPARTURES -> {
                return noticeOfDepartureMessage(PermissionMessageType.INSUFFICIENT_PERMISSION)
            }

            PermissionType.APPLICATION -> {

            }
        }

        return null
    }

    private fun ticketMessage(permissionMessageType: PermissionMessageType): MessageEmbed {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.permissions.embedTicketDeniedTitle)
            description = ColorTool().useCustomColorCodes(
                translation.permissions.embedTicketDeniedBody
                    .replace("%permission_message%", choosePermissionMessage(permissionMessageType).trimIndent())
            )
        }
        return embed
    }

    private fun noticeOfDepartureMessage(permissionMessageType: PermissionMessageType): MessageEmbed {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.permissions.embedNoticeOfDepartureDeniedTitle)
            description = ColorTool().useCustomColorCodes(
                translation.permissions.embedNoticeOfDepartureDeniedBody
                    .replace("%permission_message%", choosePermissionMessage(permissionMessageType).trimIndent())
            )
        }
        return embed
    }

    private fun choosePermissionMessage(permissionMessageType: PermissionMessageType): String {
        return when (permissionMessageType) {
            PermissionMessageType.INSUFFICIENT_PERMISSION -> translation.permissions.textInsufficientPermission

            PermissionMessageType.DEACTIVATED_ACTION -> translation.permissions.textInteractionDisabled
        }
    }
}