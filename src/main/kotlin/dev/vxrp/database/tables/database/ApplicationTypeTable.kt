package dev.vxrp.database.tables.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class ApplicationTypeTable {
    val logger = LoggerFactory.getLogger(ApplicationTypeTable::class.java)

    object ApplicationTypes : Table("application_types") {
        val roleId= text("roleId")
        val active = bool("active").default(false)
        val members = integer("members").nullable()
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
                        ApplicationTypes.deleteWhere { roleId eq currentId }
                        logger.info("Found and deleted redundant application type from database: {}", currentId)
                    }
                }
        }
    }

    fun addToDatabase(roleId: String, active: Boolean, members: Int?, initializer: String?) {
        transaction {
            ApplicationTypes.insert {
                it[ApplicationTypes.roleId] = roleId
                it[ApplicationTypes.active] = active
                it[ApplicationTypes.members] = members
                it[ApplicationTypes.initializer] = initializer
            }
        }
    }

    fun query(roleId: String): ApplicationType? {
        var applicationType: ApplicationType? = null

        transaction {
            ApplicationTypes.selectAll()
                .where { ApplicationTypes.roleId eq roleId }
                .forEach {
                    applicationType = ApplicationType(
                        it[ApplicationTypes.roleId],
                        it[ApplicationTypes.active],
                        it[ApplicationTypes.members],
                        it[ApplicationTypes.initializer]
                    )
                }
        }

        return applicationType
    }

    fun changeType(roleId: String, active: Boolean, members: Int, initializer: String?) {
        transaction {
            ApplicationTypes.update({ ApplicationTypes.roleId eq roleId }) {
                it[ApplicationTypes.active] = active
                it[ApplicationTypes.members] = members
                it[ApplicationTypes.initializer] = initializer
            }
        }
    }

    data class ApplicationType(val roleId: String,
        val active: Boolean,
        val members: Int?,
        val initializer: String?)
}