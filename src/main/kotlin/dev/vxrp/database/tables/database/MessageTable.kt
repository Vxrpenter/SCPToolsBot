/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.database.tables.database

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

    fun insertIfNotExists(id: String, type: MessageType, channelId: String) {
        transaction {
            val exists = Messages.selectAll()
                .where { Messages.id eq id }.empty()

            if (exists) {
                Messages.insert {
                    it[Messages.id] = id
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
                .where { Messages.type eq type.toString() }
                .forEach {
                    message = MessageTableData(it[Messages.id], type, it[Messages.channelId])
                }
        }
        return message
    }

    fun delete(id: String) {
        transaction {
            Messages.deleteWhere { Messages.id eq id }
        }
    }

    data class MessageTableData(
        val id: String,
        val type: MessageType,
        val channelId: String,
    )
}