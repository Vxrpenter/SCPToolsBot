package dev.vxrp.database.tables.database

import dev.vxrp.bot.status.enums.PlayerlistType
import dev.vxrp.database.data.StatusDatabaseEntry
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class StatusTable {
    object Status : Table("playerlist") {
        val type = text("type")
        val channelId = text("channel_id")
        val messageId = text("message_id")
        val port = text("port")
        val created = text("created")
        val lastUpdated = text("last_updated")
    }

    fun addToDatabase(type: PlayerlistType, channelId: String, messageId: String, port: String, created: String, lastUpdated: String) {
        transaction {
            Status.insert {
                it[Status.type] = type.toString()
                it[Status.channelId] = channelId
                it[Status.messageId] = messageId
                it[Status.port] = port
                it[Status.created] = created
                it[Status.lastUpdated] = lastUpdated
            }
        }
    }

    fun updateLastUpdated(port: String, lastUpdated: String) {
        transaction {
            Status.update({ Status.port eq port }) {
                it[Status.lastUpdated] =  lastUpdated
            }
        }
    }

    fun deleteFromDatabase(port: String) {
        transaction {
            Status.deleteWhere { Status.port eq port }
        }
    }

    fun getAllEntrys(): List<StatusDatabaseEntry> {
        val list = mutableListOf<StatusDatabaseEntry>()

        transaction {
            Status.selectAll().forEach {
                list.add(StatusDatabaseEntry(PlayerlistType.valueOf(it[Status.type]),
                    it[Status.channelId],
                    it[Status.messageId],
                    it[Status.port],
                    it[Status.created],
                    it[Status.lastUpdated]))
            }
        }

        return list
    }

    fun getType(port: String): PlayerlistType {
        var type: PlayerlistType? = null

        transaction {
            Status.selectAll()
                .where { Status.type eq PlayerlistType.PRESET.toString() }
                .forEach {
                    if (it[Status.port] == port) {
                        type = PlayerlistType.valueOf(it[Status.type])
                    }
                }
        }

        return type!!
    }
}