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

package dev.vxrp.database.tables.database

import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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

    fun addToDatabase(id: String, type: TicketType, status: TicketStatus, creationDate: String, creator: String, handler: User?, logMessage: String, message: String, statusMessage: String) {
        transaction {
            Tickets.insert {
                it[Tickets.id] = id
                it[Tickets.type] = type.toString()
                it[Tickets.status] = status.toString()
                it[Tickets.creation_date] = creationDate
                it[Tickets.creator] = creator
                it[Tickets.handler] = handler?.id
                it[Tickets.logMessage] = logMessage
                it[Tickets.message] = message
                it[Tickets.statusMessage] = statusMessage
            }
        }
    }

    fun determineTicketType(id: String): TicketType {
        var type: TicketType? = null

        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    type = TicketType.valueOf(it[Tickets.type])
                }
        }

        return type!!
    }

    fun updateTicketStatus(id: String, ticketStatus: TicketStatus) {
        transaction {
            Tickets.update({ Tickets.id eq id }) {
                it[status] = ticketStatus.toString()
            }
        }
    }

    fun getTicketCreator(id: String): String? {
        var creator: String? = null
        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    creator = it[Tickets.creator]
                }
        }

        return creator
    }

    fun getTicketHandler(id: String): String? {
        var handler: String? = null
        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    handler = it[Tickets.handler]
                }
        }

        return handler
    }

    fun getLogMessage(id: String): String? {
        var logMessage: String? = null
        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    logMessage = it[Tickets.logMessage]
                }
        }

        return logMessage
    }

    fun getMessage(id: String): String? {
        var message: String? = null
        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    message = it[Tickets.message]
                }
        }

        return message
    }

    fun getTicketStatus(id: String): TicketStatus? {
        var status: TicketStatus? = null
        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    status = TicketStatus.valueOf(it[Tickets.status])
                }
        }

        return status
    }

    fun getTicketType(id: String): TicketType? {
        var type: TicketType? = null
        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    type = TicketType.valueOf(it[Tickets.type])
                }
        }

        return type
    }

    fun updateTicketHandler(ticketId: String, userId: String) {
        transaction {
            Tickets.update({ Tickets.id eq ticketId }) {
                it[handler] = userId
            }
        }
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = Tickets.selectAll().count()
        }

        return count
    }

    fun determineHandler(id: String): Boolean {
        var bool = false

        transaction {
            Tickets.selectAll()
                .where { Tickets.id eq id }
                .forEach {
                    if (it[Tickets.handler] == null) bool = true
                }
        }

        return bool
    }
}