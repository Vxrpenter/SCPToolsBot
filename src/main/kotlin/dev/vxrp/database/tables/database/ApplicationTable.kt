/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.database.tables.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ApplicationTable {
    object Applications : Table("applications") {
        val id = text("id")
        val roleId = text("roleId")
        val state = bool("state")
        val result = bool("result")
        val issuer = text("issuer")
        val handler = text("handler").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(id: String, roleId: String, state: Boolean, result: Boolean, issuer: String, handler: String?) {
        transaction {
            Applications.insert {
                it[Applications.id] = id
                it[Applications.roleId] = roleId
                it[Applications.state] = state
                it[Applications.result] = result
                it[Applications.issuer] = issuer
                it[Applications.handler] = handler
            }
        }
    }

    fun updateTicketHandler(id: String, handler: String) {
        transaction {
            Applications.update({ Applications.id eq id }) {
                it[Applications.handler] = handler
            }
        }
    }

    fun delete(id: String) {
        transaction {
            Applications.deleteWhere { Applications.id eq id }
        }
    }

    fun retrieveSerial(roleId: String): Long {
        var count: Long = 0
        transaction {
            count = Applications.selectAll().where { Applications.roleId eq roleId }.count()
        }

        return count
    }
}