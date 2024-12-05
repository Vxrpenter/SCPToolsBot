package dev.vxrp.database.sqlite.tables

import org.jetbrains.exposed.sql.Table

class NoticeOfDepartureTable {
    object NoticeOfDepartures : Table("notice_of_departures") {
        val id = text("id")
        val handler_id = text("handler_id")
        val channel_id = text("channel_id")
        val message_id = text("message_id")
        val begin_date = text("begin_date")
        val end_date = text("end_date")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}