package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table

class TicketTable {
    object Tickets : Table("tickets") {
        val id = text("id")
        val type = text("type")
        val status = text("status").default("OPEN")
        val creation_date = text("creation_date")
        val creator = text("creator")
        val handler = text("handler").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}