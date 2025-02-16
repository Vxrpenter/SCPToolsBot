package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class ApplicationTypeTable {
    val logger = LoggerFactory.getLogger(ApplicationTypeTable::class.java)

    object ApplicationTypes : Table("application_types") {
        val roleId= text("roleId")
        val active = bool("active").default(false)
        val initializer = text("initializer").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(roleId)
    }

    fun exists(roleId: String): Boolean {
        var exists = false
        transaction {
            exists = ApplicationTypes.selectAll()
                .where { ApplicationTypes.roleId eq roleId }.empty()
        }

        return exists
    }

    fun deleteRedundantValues(roleIds: List<String>) {
        transaction {
            ApplicationTypes.selectAll()
                .forEach {
                    val currentId = it[ApplicationTypes.roleId]

                    if (!roleIds.contains(currentId)) {
                        ApplicationTypes.deleteWhere { roleId eq currentId}
                        logger.info("Found and deleted redundant application type from database: {}", currentId)
                    }
                }
        }
    }

    fun addToDatabase(roleId: String, active: Boolean, initializer: String?) {
        transaction {
            ApplicationTypes.insert {
                it[ApplicationTypes.roleId] = roleId
                it[ApplicationTypes.active] = active
                it[ApplicationTypes.initializer] = initializer
            }
        }
    }
}