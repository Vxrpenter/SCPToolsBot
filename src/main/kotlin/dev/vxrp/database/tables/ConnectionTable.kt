package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ConnectionTable {
    object Connections : Table("connections") {
        val id = text("id")
        val status = bool("status").nullable()
        val maintenance = bool("maintenance").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun insertIfNotExists(key: String, status: Boolean?, maintenance: Boolean?) {
        transaction {
            val exists = Connections.selectAll()
                .where { Connections.id eq key }.empty()

            if (exists) {
                Connections.insert {
                    it[id] = key
                    it[Connections.status] = status
                    it[Connections.maintenance] = maintenance
                }
            }
        }
    }

    data class ConnectionTableData(
        val id: String,
        val status: Boolean?,
        val maintenance: Boolean?
    )

    fun queryFromTable(key: String): ConnectionTableData {
        var status = ConnectionTableData(key, false, maintenance = false)
        transaction {
            Connections.selectAll()
                .where { Connections.id eq key}
                .forEach {
                    status = ConnectionTableData(key, it[Connections.status] == true, it[Connections.maintenance] == true)
                }
        }
        return status
    }
}