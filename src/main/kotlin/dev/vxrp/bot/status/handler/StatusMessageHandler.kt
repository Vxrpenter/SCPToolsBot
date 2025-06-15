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
            title = ColorTool().useCustomColorCodes(translation.status.embedEstablishedTitle).replace("%instance%", "Status Server System")
            description = ColorTool().useCustomColorCodes(translation.status.embedEstablishedBody)
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldValue.replace("%time%", "${info.response}"))
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedReasonFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionLost(api: JDA, retry: Int) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedLostTitle).replace("%instance%", "Status Server System")
            description = ColorTool().useCustomColorCodes(translation.status.embedLostBody.replace("%retries%", "$retry"))

            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedEstablishedResponseFieldValue.replace("%time%", "Unknown"))
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedLostReasonFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedLostReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionOnline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0x2ECC70
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedOnlineTitle).replace("%instance%", instance.name)
            description = ColorTool().useCustomColorCodes(translation.status.embedOnlineBody)

            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOnlineResponseFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedOnlineResponseFieldValue.replace("%time%", "${info?.response}"))
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOnlineReasonFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedOnlineReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }

    fun postConnectionOffline(api: JDA, instance: Instance, info: ServerInfo?) {
        val embed = Embed {
            color = 0xE74D3C
            url = config.status.pageUrl
            title = ColorTool().useCustomColorCodes(translation.status.embedOfflineTitle).replace("%instance%", instance.name)
            description = ColorTool().useCustomColorCodes(translation.status.embedOfflineBody.replace("%retries%", instance.retries.toString()))

            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOfflineResponseFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedOfflineResponseFieldValue.replace("%time%", "${info?.response}"))
            }
            field {
                name = ColorTool().useCustomColorCodes(translation.status.embedOfflineReasonFieldName)
                value = ColorTool().useCustomColorCodes(translation.status.embedOfflineReasonFieldValue)
            }
        }

        api.getTextChannelById(config.status.postChannel)?.sendMessageEmbeds(embed)?.queue()
    }
}