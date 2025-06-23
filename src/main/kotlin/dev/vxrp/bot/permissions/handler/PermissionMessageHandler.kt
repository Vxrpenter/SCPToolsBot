/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

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
                    title = ColorTool().parse(translation.permissions.embedCouldNotSendPanelTitle)
                    description = ColorTool().parse(translation.permissions.embedCouldNotSendPanelBody)
                }
            }

            StatusMessageType.MODAL -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().parse(translation.permissions.embedCouldNotSendModalTitle)
                    description = ColorTool().parse(translation.permissions.embedCouldNotSendModalBody)
                }
            }

            StatusMessageType.COMMAND -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().parse(translation.permissions.embedCouldNotSendCommandTitle)
                    description = ColorTool().parse(translation.permissions.embedCouldNotSendCommandBody)
                }
            }

            StatusMessageType.TEMPLATE -> {
                return Embed {
                    color = 0xE74D3C
                    title = ColorTool().parse(translation.permissions.embedCouldNotSendTemplateTitle)
                    description = ColorTool().parse(translation.permissions.embedCouldNotSendTemplateBody)
                }
            }
        }
    }

    private fun ticketMessage(permissionMessageType: PermissionMessageType): MessageEmbed {
        val embed = Embed {
            title = ColorTool().parse(translation.permissions.embedTicketDeniedTitle)
            description = ColorTool().parse(
                translation.permissions.embedTicketDeniedBody
                    .replace("%permission_message%", choosePermissionMessage(permissionMessageType).trimIndent())
            )
        }
        return embed
    }

    private fun noticeOfDepartureMessage(permissionMessageType: PermissionMessageType): MessageEmbed {
        val embed = Embed {
            title = ColorTool().parse(translation.permissions.embedNoticeOfDepartureDeniedTitle)
            description = ColorTool().parse(
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