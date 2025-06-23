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

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApplicationTypeTable {
    val logger: Logger = LoggerFactory.getLogger(ApplicationTypeTable::class.java)

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

    fun getAllEntrys(): List<ApplicationType>? {
        val typeList = mutableListOf<ApplicationType>()

        transaction {
            ApplicationTypes.selectAll()
                .forEach {
                    typeList.add(ApplicationType(
                        it[ApplicationTypes.roleId],
                        it[ApplicationTypes.active],
                        it[ApplicationTypes.members],
                        it[ApplicationTypes.initializer]
                    ))
                }
        }

        return typeList
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