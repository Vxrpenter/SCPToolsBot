package dev.vxrp.database.tables

import dev.vxrp.bot.application.enums.MessageType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MessageTable {
    object Messages : Table("messages") {
        val id = text("id")
        val type = text("type")
        val channelId = text("channelId")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun insertIfNotExists(key: String, type: MessageType, channelId: String) {
        transaction {
            val exists = Messages.selectAll()
                .where { Messages.id eq key }.empty()

            if (exists) {
                Messages.insert {
                    it[id] = key
                    it[Messages.type] = type.toString()
                    it[Messages.channelId] = channelId
                }
            }
        }
    }

    fun queryFromTable(type: MessageType): MessageTableData? {
        var message: MessageTableData? = null
        transaction {
            Messages.selectAll()
                .where { Messages.type eq type.toString()}
                .forEach {
                    message = MessageTableData(it[Messages.id], type, it[Messages.channelId])
                }
        }
        return message
    }

    fun delete(id: String) {
        transaction {
            Messages.deleteWhere {Messages.id eq id}
        }
    }

    data class MessageTableData(
        val id: String,
        val type: MessageType,
        val channelId: String,
    )
}