package dev.vxrp.database.sqlite.tables

import org.jetbrains.exposed.sql.Table

class ConnectionTable {
    object Connections : Table("connections") {
        val id = text("id")
        val status = bool("status")
        val maintenance = bool("maintenance")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}