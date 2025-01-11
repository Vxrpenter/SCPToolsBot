package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table

class ActionQueueTable {
    object ActionQueue : Table("action_queue") {
        val id = text("id")
        val command = text("command")
        val date_added = text("date_added")
        val processed = bool("processed").default(false)

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}