package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class NoticeOfDepartureTable {
    object NoticeOfDepartures : Table("notice_of_departures") {
        val id = text("id")
        val handlerId = text("handler_id")
        val channelId = text("channel_id")
        val messageId = text("message_id")
        val beginDate = text("begin_date")
        val endDate = text("end_date")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(noticeId: String, handler: String, channel: String, message: String, exactBeginDate: String, exactEndDate: String) {
        transaction {
            NoticeOfDepartures.insert {
                it[id] = noticeId
                it[handlerId] = handler
                it[channelId] = channel
                it[messageId] = message
                it[beginDate] = exactBeginDate
                it[endDate] = exactEndDate
            }
        }
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = NoticeOfDepartures.selectAll().count()
        }

        return count
    }
}