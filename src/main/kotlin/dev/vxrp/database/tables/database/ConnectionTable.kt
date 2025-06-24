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

import dev.vxrp.database.data.ConnectionDatabaseEntry
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ConnectionTable {
    object Connections : Table("connections") {
        val id = text("id")
        val status = bool("status").nullable()
        val maintenance = bool("maintenance").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun insertIfNotExists(id: String, status: Boolean?, maintenance: Boolean?) {
        transaction {
            val exists = Connections.selectAll()
                .where { Connections.id eq id }.empty()

            if (exists) {
                Connections.insert {
                    it[Connections.id] = id
                    it[Connections.status] = status
                    it[Connections.maintenance] = maintenance
                }
            }
        }
    }

    fun queryFromTable(id: String): ConnectionDatabaseEntry {
        var status = ConnectionDatabaseEntry(id, false, maintenance = false)
        transaction {
            Connections.selectAll()
                .where { Connections.id eq id }
                .forEach {
                    status =
                        ConnectionDatabaseEntry(id, it[Connections.status] == true, it[Connections.maintenance] == true)
                }
        }
        return status
    }

    fun databaseNotExists(id: String, status: Boolean) {
        transaction {
            val exists = Connections.selectAll()
                .where { Connections.id.eq(id) }.empty()

            if (exists) {
                Connections.insert {
                    it[Connections.id] = id
                    it[Connections.status] = status
                }
            }
        }
    }

    fun postConnectionToDatabase(id: String, status: Boolean) {
        transaction {
            Connections.update({ Connections.id eq id }) {
                it[Connections.status] = status
            }
        }
    }

    fun setMaintenance(id: String, maintenance: Boolean?) {
        transaction {
            Connections.update({ Connections.id eq id }) {
                it[Connections.maintenance] = maintenance
            }
        }
    }
}