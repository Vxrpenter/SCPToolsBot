package dev.vxrp.database.sqlite.tables

import org.jetbrains.exposed.sql.Table

class Ticket {
    object Tickets : Table("tickets") {
        val id = text("id")
        val identifier = text("identifier")
        val status = text("status").default("OPEN")
        val creation_date = text("creation_date")
        val creator_id = text("creator_id")
        val handler_id = text("handler_id")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}