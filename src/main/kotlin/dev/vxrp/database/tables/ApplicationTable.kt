package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table

class ApplicationTable {
    object Applications : Table("applications") {
        val id = text("id")
        val state = text("state")
        val issuer = text("issuer")
        val handler = text("handler")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}