package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.tables.TicketTable
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDate

class TicketHandler(val api: JDA, val config: Config, private val ticketType: TicketType, private val ticketStatus: TicketStatus, private val ticketCreator: User, private val ticketHandler: User?) {
    private val logger = LoggerFactory.getLogger(TicketHandler::class.java)
    val settings = querySettings()

    suspend fun createTicket() {
        if (settings == null) {
            logger.error("Can't create ticket, settings are not loaded")
            return
        }

        val channel = api.getTextChannelById(settings.parentChannel)
        if (channel == null) {
            logger.error("Could not find specified parent channel for '{}' ticket", settings.name)
            return
        }

        val child = channel.createThreadChannel(settings.childRules.parentName.replace("%r%", retrieveSerial().toString()), true).await()

        for (roleId in settings.roles) {
            val role = api.getRoleById(roleId)

            if (role == null) {
                logger.error("Could not find role {} for ticket {}, does it exist?", roleId, settings.name)
                continue
            }

            child.sendMessage(role.asMention).await().delete().queue()
            addToDatabase(child.id, LocalDate.now())
        }
    }

    fun deleteTicket(id: String) {
        if (settings == null) {
            logger.error("Can't delete ticket, settings are not loaded")
            return
        }

        api.getThreadChannelById(id)!!.delete().queue()
        deleteTicket(id)
    }

    private fun addToDatabase(ticketId: String, date: LocalDate) {
        transaction {
            TicketTable.Tickets.insert {
                it[id] = ticketId
                it[type] = ticketType.toString()
                it[status] = ticketStatus.toString()
                it[creation_date] = date.toString()
                it[creator] = ticketCreator.id
                it[handler] = ticketHandler?.id
            }
        }
    }

    fun deleteFromDatabase(ticketId: String) {
        transaction {
            TicketTable.Tickets.deleteWhere { id.eq(ticketId) }
        }
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = TicketTable.Tickets.selectAll().count()
        }

        return count
    }

    private fun querySettings(): TicketSettings? {
        for (option in config.ticket.settings) {
            if (option.type.replace("support::", "") != ticketType.toString()) continue
            return option
        }

        logger.error("Could not correctly load ticket settings")
        return null
    }
}