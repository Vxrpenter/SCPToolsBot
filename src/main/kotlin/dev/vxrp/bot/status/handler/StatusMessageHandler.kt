package dev.vxrp.bot.status.handler

import dev.minn.jda.ktx.messages.Embed
import io.github.vxrpenter.secretlab.data.ServerInfo
import dev.vxrp.bot.status.data.Instance
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA

class StatusMessageHandler(val config: Config, val translation: Translation) {
    fun postConnectionEstablished(api: JDA, info: ServerInfo) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedEstablishedTitle)
                .replace("%instance%", "Status Server System").trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedEstablishedBody).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldValue
                    .replace("%time%", "${info.response}")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedReasonFieldValue).trimIndent()
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionLost(api: JDA, retry: Int) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedLostTitle)
                .replace("%instance%", "Status Server System").trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedLostBody
                    .replace("%retries%", "$retry")
            ).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldValue
                    .replace("%time%", "999")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedLostReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedLostReasonFieldValue).trimIndent()
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionOnline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedOnlineTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(translation.status.embedOnlineBody).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOnlineResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOnlineResponseFieldValue
                    .replace("%time%", "${info?.response}")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOnlineReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOnlineReasonFieldValue).trimIndent()
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionOffline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedOfflineTitle)
                .replace("%instance%", instance.name).trimIndent()
            description = ColorTool().useCustomColorCodes(
                translation.status.embedOfflineBody
                    .replace("%retries%", instance.retries.toString())
            ).trimIndent()
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOfflineResponseFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOfflineResponseFieldValue
                    .replace("%time%", "${info?.response}")).trimIndent()
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOfflineReasonFieldName).trimIndent()
                value = ColorTool().useCustomColorCodes(translation.status.embedOfflineReasonFieldValue).trimIndent()
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }
}