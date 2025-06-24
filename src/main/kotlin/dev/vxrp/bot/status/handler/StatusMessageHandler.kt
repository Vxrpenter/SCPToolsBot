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

package dev.vxrp.bot.status.handler

import dev.minn.jda.ktx.messages.Embed
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import io.github.vxrpenter.secretlab.data.ServerInfo
import net.dv8tion.jda.api.JDA

class StatusMessageHandler(val config: Config, val translation: Translation) {
    fun postConnectionEstablished(api: JDA, info: ServerInfo) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
            title = ColorTool().parse(translation.status.embedEstablishedTitle).replace("%instance%", "Status Server System")
            description = ColorTool().parse(translation.status.embedEstablishedBody)
            field {
                name = ColorTool().parse(translation.status.embedEstablishedResponseFieldName)
                value = ColorTool().parse(translation.status.embedEstablishedResponseFieldValue.replace("%time%", "${info.response}"))
            }
            field {
                name = ColorTool().parse(translation.status.embedEstablishedReasonFieldName)
                value = ColorTool().parse(translation.status.embedEstablishedReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionLost(api: JDA, retry: Int) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().parse(translation.status.embedLostTitle).replace("%instance%", "Status Server System")
            description = ColorTool().parse(translation.status.embedLostBody.replace("%retries%", "$retry"))

            field {
                name = ColorTool().parse(translation.status.embedEstablishedResponseFieldName)
                value = ColorTool().parse(translation.status.embedEstablishedResponseFieldValue.replace("%time%", "Unknown"))
            }
            field {
                name = ColorTool().parse(translation.status.embedLostReasonFieldName)
                value = ColorTool().parse(translation.status.embedLostReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionOnline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
            title = ColorTool().parse(translation.status.embedOnlineTitle).replace("%instance%", instance.name)
            description = ColorTool().parse(translation.status.embedOnlineBody)

            field {
                name = ColorTool().parse(translation.status.embedOnlineResponseFieldName)
                value = ColorTool().parse(translation.status.embedOnlineResponseFieldValue.replace("%time%", "${info?.response}"))
            }
            field {
                name = ColorTool().parse(translation.status.embedOnlineReasonFieldName)
                value = ColorTool().parse(translation.status.embedOnlineReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionOffline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().parse(translation.status.embedOfflineTitle).replace("%instance%", instance.name)
            description = ColorTool().parse(translation.status.embedOfflineBody.replace("%retries%", instance.retries.toString()))

            field {
                name = ColorTool().parse(translation.status.embedOfflineResponseFieldName)
                value = ColorTool().parse(translation.status.embedOfflineResponseFieldValue.replace("%time%", "${info?.response}"))
            }
            field {
                name = ColorTool().parse(translation.status.embedOfflineReasonFieldName)
                value = ColorTool().parse(translation.status.embedOfflineReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }
}