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

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class NoticeOfDepartureTable {
    object NoticeOfDepartures : Table("notice_of_departures") {
        val id = text("id")
        val active = bool("active")
        val handlerId = text("handler_id")
        val channelId = text("channel_id")
        val messageId = text("message_id")
        val beginDate = text("begin_date")
        val endDate = text("end_date")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(id: String, active: Boolean, handlerId: String, channelId: String, messageId: String, beginDate: String, endDate: String) {
        transaction {
            NoticeOfDepartures.insert {
                it[NoticeOfDepartures.id] = id
                it[NoticeOfDepartures.active] = active
                it[NoticeOfDepartures.handlerId] = handlerId
                it[NoticeOfDepartures.channelId] = channelId
                it[NoticeOfDepartures.messageId] = messageId
                it[NoticeOfDepartures.beginDate] = beginDate
                it[NoticeOfDepartures.endDate] = endDate
            }
        }
    }

    fun deleteEntry(id: String) {
        transaction {
            NoticeOfDepartures.deleteWhere { NoticeOfDepartures.id eq id }
        }
    }

    fun retrieveAllIds(): List<String> {
        val list = mutableListOf<String>()

        transaction {
            NoticeOfDepartures.selectAll()
                .forEach {
                    list.add(it[NoticeOfDepartures.id])
                }
        }

        return list
    }

    fun retrieveHandler(id: String): String? {
        var handler: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq id }
                .forEach {
                    handler = it[NoticeOfDepartures.handlerId]
                }
        }

        return handler
    }

    fun retrieveChannel(id: String): String? {
        var channel: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq id }
                .forEach {
                    channel = it[NoticeOfDepartures.channelId]
                }
        }

        return channel
    }

    fun retrieveMessage(id: String): String? {
        var message: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq id }
                .forEach {
                    message = it[NoticeOfDepartures.messageId]
                }
        }

        return message
    }

    fun retrieveBeginDate(id: String): String? {
        var date: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq id }
                .forEach {
                    date = it[NoticeOfDepartures.beginDate]
                }
        }

        return date
    }

    fun retrieveEndDate(id: String): String? {
        var date: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq id }
                .forEach {
                    date = it[NoticeOfDepartures.endDate]
                }
        }

        return date
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = NoticeOfDepartures.selectAll().count()
        }

        return count
    }

    fun exists(id: String): Boolean {
        var exists = false
        transaction {
            exists = !NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq id }.empty()
        }

        return exists
    }
}