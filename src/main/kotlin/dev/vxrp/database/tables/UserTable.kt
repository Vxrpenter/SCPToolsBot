package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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
        if (exists(userId)) return

        transaction {
            Users.insert {
                it[id] = userId
                it[verifyTime] = verifyTimestamp
                it[steamId] = userSteamId
                it[refreshToken] = discordRefreshToken
            }
        }
    }

    fun exists(id: String): Boolean {
        var exists = false
        transaction {
            exists = Users.selectAll()
                .where { Users.id eq id }.empty()
        }

        return exists
    }
}