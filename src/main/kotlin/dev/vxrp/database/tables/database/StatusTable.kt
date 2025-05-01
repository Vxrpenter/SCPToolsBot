package dev.vxrp.database.tables.database

import dev.vxrp.bot.regulars.data.RegularDatabaseEntry
import dev.vxrp.bot.status.enums.PlayerlistType
import dev.vxrp.database.data.StatusDatabaseEntry
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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
                it[this.type] = type.toString()
                it[this.channelId] = channelId
                it[this.messageId] = messageId
                it[this.port] = port
                it[this.created] = created
                it[this.lastUpdated] = lastUpdated
            }
        }
    }

    fun updateLastUpdated(port: String, lastUpdated: String) {
        transaction {
            Status.update({ Status.port eq port }) {
                it[this.lastUpdated] =  lastUpdated
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