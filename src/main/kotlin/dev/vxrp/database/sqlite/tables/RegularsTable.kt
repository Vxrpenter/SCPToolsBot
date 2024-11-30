package dev.vxrp.database.sqlite.tables

import org.jetbrains.exposed.sql.Table

class RegularsTable {
    object Regulars : Table("regulars") {
        val id = text("id")
        val active = bool("active").default(true)
        val steam_id = text("steam_id")
        val group_role_id = text("group_id")
        val role_id = text("role_id")
        val last_checked_date = text("last_checked_date")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}