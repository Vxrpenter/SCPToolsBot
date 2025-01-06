package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.modals.ModalMapping
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate

class TicketHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(TicketHandler::class.java)

    suspend fun createTicket(ticketType: TicketType, ticketStatus: TicketStatus, ticketCreator: String, ticketHandler: User?, modalId: String, modalValue: MutableList<ModalMapping>): ThreadChannel? {
        val settings = querySettings(ticketType)
        if (settings == null) {
            logger.error("Can't create ticket, settings are not loaded")
            return null
        }

        val channel = api.getTextChannelById(settings.parentChannel)
        if (channel == null) {
            logger.error("Could not find specified parent channel for '{}' ticket", settings.name)
            return null
        }

        val child = channel.createThreadChannel(settings.childRules.parentName.replace("%r%", retrieveSerial().toString()), true).await()
        if (ticketCreator != "anonymous") {
            val creatorUser = api.retrieveUserById(ticketCreator).await()
            child.sendMessage(creatorUser.asMention).await().delete().queue()
        }
        for (roleId in settings.roles) {
            val role = api.getRoleById(roleId)

            if (role == null) {
                logger.error("Could not find role {} for ticket {}, does it exist?", roleId, settings.name)
                continue
            }

            child.sendMessage(role.asMention).await().delete().queue()
        }

        addToDatabase(child.id, LocalDate.now(), ticketType, ticketStatus, ticketCreator, ticketHandler)
        sendMessage(ticketType, child, ticketCreator, modalId, modalValue)
        return child
    }

    private suspend fun sendMessage(type: TicketType, channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>) {
        when(type) {
            TicketType.GENERAL -> {
                val embed = Embed {
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

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings)
                ).queue()
            }

            TicketType.REPORT -> {
                val embed = Embed {
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

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings)
                ).queue()
            }

            TicketType.ERROR -> {
                val embed = Embed {
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

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings)
                ).queue()
            }

            TicketType.UNBAN -> {
                val embed = Embed {
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

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings)
                ).queue()
            }

            TicketType.COMPLAINT -> {
                var staff = "Anonymous"
                if (modalId.split(":")[1] != "anonymous") {
                    staff = "<@${modalId.split(":")[1]}>"
                }

                val embed = Embed {
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
                        .replace("%issuer%", "<@$userId>")
                        .replace("%staff%", staff)
                        .replace("%reason%", modalValues[0].asString)
                        .replace("%proof%", modalValues[1].asString))
                    timestamp = Instant.now()
                }

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings)
                ).queue()
            }
        }
    }

    private fun addToDatabase(ticketId: String, date: LocalDate, ticketType: TicketType, ticketStatus: TicketStatus, ticketCreator: String, ticketHandler: User?) {
        transaction {
            TicketTable.Tickets.insert {
                it[id] = ticketId
                it[type] = ticketType.toString()
                it[status] = ticketStatus.toString()
                it[creation_date] = date.toString()
                it[creator] = ticketCreator
                it[handler] = ticketHandler?.id
            }
        }
    }

    private fun updateTicketStatus(ticketId: String, ticketStatus: TicketStatus) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[status] = ticketStatus.toString()
            }
        }
    }

    private fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = TicketTable.Tickets.selectAll().count()
        }

        return count
    }

    private fun querySettings(ticketType: TicketType): TicketSettings? {
        for (option in config.ticket.settings) {
            if (option.type.replace("support::", "") != ticketType.toString()) continue
            return option
        }

        logger.error("Could not correctly load ticket settings")
        return null
    }

    fun deleteTicket(id: String, ticketType: TicketType) {
        val settings = querySettings(ticketType)
        if (settings == null) {
            logger.error("Can't delete ticket, settings are not loaded")
            return
        }
        api.getThreadChannelById(id)!!.manager.setArchived(true).queue()
        updateTicketStatus(id, TicketStatus.CLOSED)
    }

    fun claimTicket(id: String) {

    }
}