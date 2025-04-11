package dev.vxrp.database.tables.xp

import org.jetbrains.exposed.sql.Table

class PlayerInfoTable {
    object PlayerInfoSteam : Table("playerinfo") {
        val id = long("id")
        val xp = integer("xp").default(0)
        val nickname = varchar("nickname", 64)

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    object PlayerInfoDiscord: Table("playerinfo") {
        val id = long("id")
        val xp = integer("xp").default(0)
        val nickname = varchar("nickname", 64)

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    object PlayerInfoNorthwood : Table("playerinfo") {
        val id = varchar("id", 32)
        val xp = integer("xp").default(0)
        val nickname = varchar("nickname", 64)

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}