package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ApplicationTable {
    object Applications : Table("applications") {
        val id = text("id")
        val state = bool("state")
        val issuer = text("issuer")
        val handler = text("handler").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(id: String, state: Boolean, issuer: String, handler: String?) {
        transaction {
            Applications.insert {
                it[Applications.id] = id
                it[Applications.state] = state
                it[Applications.issuer] = issuer
                it[Applications.handler] = handler
            }
        }
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = Applications.selectAll().count()
        }

        return count
    }
}