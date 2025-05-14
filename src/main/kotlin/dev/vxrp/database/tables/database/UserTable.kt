package dev.vxrp.database.tables.database

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserTable {
    object Users : Table("users") {
        val id = text("id")
        val verifyTime = text("verify_time")
        val steamId = text("steam_id")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(id: String, verifyTime: String, steamId: String) {
        if (exists(id)) return

        transaction {
            Users.insert {
                it[Users.id] = id
                it[Users.verifyTime] = verifyTime
                it[Users.steamId] = steamId
            }
        }
    }

    fun getVerifyTime(id: String): String {
        var verifiedTime: String? = null

        transaction {
            Users.selectAll()
                .where { Users.id eq id }
                .forEach {
                    verifiedTime = it[Users.verifyTime]
                }
        }

        return verifiedTime!!
    }

    fun getSteamId(id: String): String? {
        if (!exists(id)) return null
        var steamId: String? = null

        transaction {
            Users.selectAll()
                .where { Users.id eq id }
                .forEach {
                    steamId = it[Users.steamId]
                }
        }

        return steamId!!
    }

    fun delete(id: String) {
        if (!exists(id)) return

        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

    fun exists(id: String): Boolean {
        var exists = false
        transaction {
            exists = !Users.selectAll()
                .where { Users.id eq id }.empty()
        }

        return exists
    }
}