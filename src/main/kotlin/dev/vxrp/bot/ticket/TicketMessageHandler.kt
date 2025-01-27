package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.modals.ModalMapping
import java.time.Instant

class TicketMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    suspend fun sendMessage(type: TicketType, channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>) {
        when(type) {
            TicketType.GENERAL -> {
                channel.send("", listOf(generalMessage(channel, userId, modalValues))).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.REPORT -> {
                channel.send("", listOf(reportMessage(channel, userId, modalId, modalValues))).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.ERROR -> {
                channel.send("", listOf(errorMessage(channel, userId, modalValues))).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.UNBAN -> {
                channel.send("", listOf(unbanMessage(channel, userId, modalValues))).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.COMPLAINT -> {
                channel.send("", listOf(complaintMessage(channel, userId, modalId, modalValues))).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }
        }
    }

    private suspend fun generalMessage(channel: ThreadChannel, userId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketGeneralTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketGeneralBody
                .replace("%issuer%", "<@$userId>")
                .replace("%subject%", modalValues[0].asString)
                .replace("%explanation%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun reportMessage(channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketReportTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketReportBody
                .replace("%issuer%", "<@$userId>")
                .replace("%reported%", "<@${modalId.split(":")[1]}>")
                .replace("%reason%", modalValues[0].asString)
                .replace("%proof%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun errorMessage(channel: ThreadChannel, userId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketErrorTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketErrorBody
                .replace("%issuer%", "<@$userId>")
                .replace("%problem%", modalValues[0].asString)
                .replace("%times%", modalValues[1].asString)
                .replace("%reproduce%", modalValues[2].asString)
                .replace("%additional%", modalValues[3].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun unbanMessage(channel: ThreadChannel, userId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        return Embed {
            author {
                val user = api.retrieveUserById(userId).await()
                iconUrl = user.avatarUrl
                name = user.name
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketUnbanTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketUnbanBody
                .replace("%issuer%", "<@$userId>")
                .replace("%steamId%", modalValues[0].asString)
                .replace("%reason%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }

    private suspend fun complaintMessage(channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>): MessageEmbed {
        var staff = "Anonymous"
        if (modalId.split(":")[1] != "anonymous") {
            staff = "<@${modalId.split(":")[1]}>"
        }

        return Embed {
            author {
                if (userId != "anonymous") {
                    val user = api.retrieveUserById(userId).await()
                    iconUrl = user.avatarUrl
                    name = user.globalName
                } else {
                    iconUrl = "https://www.pngarts.com/files/4/Anonymous-Mask-Transparent-Images.png"
                    name = "Anonymous"
                }
            }
            title = ColorTool().useCustomColorCodes(translation.support.embedTicketComplaintTitle.replace("%name%", channel.name))
            description = ColorTool().useCustomColorCodes(translation.support.embedTicketComplaintBody
                .replace("%issuer%", "<@$userId>".replace("<@anonymous>", "**Anonymous**"))
                .replace("%staff%", staff)
                .replace("%reason%", modalValues[0].asString)
                .replace("%proof%", modalValues[1].asString))
            timestamp = Instant.now()
        }
    }
}