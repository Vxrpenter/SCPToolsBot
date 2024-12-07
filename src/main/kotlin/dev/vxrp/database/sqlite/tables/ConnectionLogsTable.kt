package dev.vxrp.database.sqlite.tables

import org.jetbrains.exposed.sql.Table

class ConnectionLogsTable {
    object ConnectionLogs: Table("connectionlogs") {
        val id = text("id")
        val port = integer("port")
        val concluded = bool("concluded").default(false)
        val lostTime = long("lostTime")
        val regainTime = long("regainTime")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}