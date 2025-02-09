package dev.vxrp.database.tables

import dev.vxrp.database.tables.ConnectionTable.Connections.nullable
import org.jetbrains.exposed.sql.Table

class MessageTable {
    object Messages : Table("messages") {
        val id = text("id")
        val type = text("type")
        val channelId = text("channelid")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}