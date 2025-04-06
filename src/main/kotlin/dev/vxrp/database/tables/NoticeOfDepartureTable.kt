package dev.vxrp.database.tables

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

    fun addToDatabase(noticeId: String, activeNotice: Boolean, handler: String, channel: String, message: String, exactBeginDate: String, exactEndDate: String) {
        transaction {
            NoticeOfDepartures.insert {
                it[id] = noticeId
                it[active] = activeNotice
                it[handlerId] = handler
                it[channelId] = channel
                it[messageId] = message
                it[beginDate] = exactBeginDate
                it[endDate] = exactEndDate
            }
        }
    }

    fun deleteEntry(noticeId: String) {
        transaction {
            NoticeOfDepartures.deleteWhere { id eq noticeId }
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

    fun retrieveBeginDate(noticeId: String): String? {
        var date: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq noticeId }
                .forEach {
                    date = it[NoticeOfDepartures.beginDate]
                }
        }

        return date
    }

    fun retrieveEndDate(noticeId: String): String? {
        var date: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where { NoticeOfDepartures.id eq noticeId }
                .forEach {
                    date = it[NoticeOfDepartures.endDate]
                }
        }

        return date
    }

    fun retrieveChannel(noticeId: String): String? {
        var channel: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where {NoticeOfDepartures.id eq noticeId}
                .forEach {
                    channel = it[NoticeOfDepartures.channelId]
                }
        }

        return channel
    }

    fun retrieveMessage(noticeId: String): String? {
        var message: String? = null

        transaction {
            NoticeOfDepartures.selectAll()
                .where {NoticeOfDepartures.id eq noticeId}
                .forEach {
                    message = it[NoticeOfDepartures.messageId]
                }
        }

        return message
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = NoticeOfDepartures.selectAll().count()
        }

        return count
    }
}