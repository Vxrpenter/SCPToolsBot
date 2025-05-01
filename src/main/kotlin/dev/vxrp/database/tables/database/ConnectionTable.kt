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
}