package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class UserTable {
    object Users : Table("users") {
        val id = text("id")
        val verifyTime = text("verify_time")
        val steamId = text("steam_id")
        val refreshToken = text("refresh_token")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(userId: String, verifyTimestamp: String, userSteamId: String, discordRefreshToken: String) {
        transaction {
            Users.insert {
                it[id] = userId
                it[verifyTime] = verifyTimestamp
                it[steamId] = userSteamId
                it[refreshToken] = discordRefreshToken
            }
        }
    }
}