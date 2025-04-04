package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table

class UserTable {
    object Users : Table("users") {
        val id = text("id")
        val verifyTime = text("verify_time")
        val steamId = text("steam_id")
        val refreshToken = text("refresh_token")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}