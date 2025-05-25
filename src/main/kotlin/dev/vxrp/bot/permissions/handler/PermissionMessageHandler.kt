package dev.vxrp.bot.permissions.handler

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.permissions.enums.PermissionMessageType
import dev.vxrp.bot.permissions.enums.PermissionType
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.MessageEmbed

class PermissionMessageHandler(val config: Config, val translation: Translation) {
    fun getPermissionMessage(permissionType: PermissionType): MessageEmbed? {
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

    fun getStatusMessage(messageType: StatusMessageType): MessageEmbed {
        when (messageType) {
            StatusMessageType.PANEL -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendPanelTitle)
                    description = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendPanelBody)
                }
            }

            StatusMessageType.MODAL -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendModalTitle)
                    description = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendModalBody)
                }
            }

            StatusMessageType.COMMAND -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendCommandTitle)
                    description = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendCommandBody)
                }
            }

            StatusMessageType.TEMPLATE -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendTemplateTitle)
                    description = ColorTool().useCustomColorCodes(translation.permissions.embedCouldNotSendTemplateBody)
                }
            }
        }
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