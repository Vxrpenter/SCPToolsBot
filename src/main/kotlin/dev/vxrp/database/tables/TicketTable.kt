package dev.vxrp.database.tables

import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate

class TicketTable {
    object Tickets : Table("tickets") {
        val id = text("id")
        val type = text("type")
        val status = text("status").default("OPEN")
        val creation_date = text("creation_date")
        val creator = text("creator")
        val handler = text("handler").nullable()
        val logMessage = text("log_message")
        val statusMessage = text("status_message")
        val message = text("message")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(ticketId: String, date: LocalDate, ticketType: TicketType, ticketStatus: TicketStatus, ticketCreator: String, ticketHandler: User?, ticketLogMessage: String, ticketMessage: String, ticketStatusMessage: String) {
        transaction {
            Tickets.insert {
                it[id] = ticketId
                it[type] = ticketType.toString()
                it[status] = ticketStatus.toString()
                it[creation_date] = date.toString()
                it[creator] = ticketCreator
                it[handler] = ticketHandler?.id
                it[logMessage] = ticketLogMessage
                it[message] = ticketMessage
                it[statusMessage] = ticketStatusMessage
            }
        }
    }

    fun determineTicketType(ticketId: String): TicketType {
        var type: TicketType? = null

        transaction {
            Tickets.selectAll()
                .where {Tickets.id eq ticketId}
                .forEach {
                    type = TicketType.valueOf(it[Tickets.type])
                }
        }

        return type!!
    }

    fun updateTicketStatus(ticketId: String, ticketStatus: TicketStatus) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[status] = ticketStatus.toString()
            }
        }
    }

    fun getTicketStatus(ticketId: String): TicketStatus? {
        var status: TicketStatus? = null
        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    status = TicketStatus.valueOf(it[TicketTable.Tickets.status])
                }
        }

        return status
    }

    fun getTicketType(ticketId: String): TicketType? {
        var type: TicketType? = null
        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    type = TicketType.valueOf(it[TicketTable.Tickets.type])
                }
        }

        return type
    }

    fun updateTicketHandler(ticketId: String, userId: String) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[handler] = userId
            }
        }
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = TicketTable.Tickets.selectAll().count()
        }

        return count
    }

    fun determineHandler(ticketId: String): Boolean {
        var bool = false

        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    if (it[TicketTable.Tickets.handler] == null) bool = true
                }
        }

        return bool
    }
}