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

import dev.vxrp.bot.regulars.data.RegularDatabaseEntry
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class RegularsTable {
    object Regulars : Table("regulars") {
        val id = text("id")
        val active = bool("active").default(true)
        val group = text("group")
        val groupRoleId = text("group_id").nullable()
        val roleId = text("role_id")
        val playtime = double("playtime")
        val level = integer("level")
        val lastCheckedDate = text("last_checked_date").nullable()

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(id: String, active: Boolean, group: String, groupRoleId: String?, roleId: String, playtime: Double, level: Int, lastCheckedDate: String?) {
        if (exists(id)) return

        transaction {
            Regulars.insert {
                it[Regulars.id] = id
                it[Regulars.active] = active
                it[Regulars.group] = group
                it[Regulars.groupRoleId] = groupRoleId
                it[Regulars.roleId] = roleId
                it[Regulars.playtime] = playtime
                it[Regulars.level] = level
                it[Regulars.lastCheckedDate] = lastCheckedDate
            }
        }
    }

    fun getEntry(id: String): RegularDatabaseEntry? {
        var entry: RegularDatabaseEntry? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    entry = RegularDatabaseEntry(
                        it[Regulars.id],
                        it[Regulars.active],
                        it[Regulars.group],
                        it[Regulars.groupRoleId],
                        it[Regulars.roleId],
                        it[Regulars.lastCheckedDate],
                        it[Regulars.playtime])
                }
        }

        return entry
    }

    fun getAllEntrys(): List<RegularDatabaseEntry> {
        val list = mutableListOf<RegularDatabaseEntry>()

        transaction {
            Regulars.selectAll().forEach {
                list.add(RegularDatabaseEntry(
                    it[Regulars.id],
                    it[Regulars.active],
                    it[Regulars.group],
                    it[Regulars.groupRoleId],
                    it[Regulars.roleId],
                    it[Regulars.lastCheckedDate],
                    it[Regulars.playtime]))
            }
        }

        return list
    }

    fun setActive(id: String, activity: Boolean) {
        transaction {
            Regulars.update({ Regulars.id eq id }) {
                it[active] = activity
            }
        }
    }

    fun setPlaytime(id: String, playtime: Double) {
        transaction {
            Regulars.update({ Regulars.id eq id }) {
                it[Regulars.playtime] = playtime
            }
        }
    }

    fun setLevel(id: String, level: Int) {
        transaction {
            Regulars.update({ Regulars.id eq id }) {
                it[Regulars.level] = level
            }
        }
    }

    fun setLastCheckedDate(id: String, lastCheckedDate: String) {
        transaction {
            Regulars.update({ Regulars.id eq id }) {
                it[Regulars.lastCheckedDate] = lastCheckedDate
            }
        }
    }

    fun getActive(id: String): Boolean {
        var active: Boolean? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    active = it[Regulars.active]
                }
        }

        return active!!
    }

    fun getGroupRole(id: String): String? {
        var groupRole: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    groupRole = it[Regulars.groupRoleId]
                }
        }

        return groupRole
    }

    fun getGroup(id: String): String {
        var groupName: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    groupName = it[Regulars.group]
                }
        }

        return groupName!!
    }

    fun getRole(id: String): String? {
        var role: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    role = it[Regulars.roleId]
                }
        }

        return role
    }

    fun getPlaytime(id: String): Double {
        var playtime: Double? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    playtime = it[Regulars.playtime]
                }
        }

        return playtime!!
    }

    fun getLevel(id: String): Int {
        var xpLevel: Int? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    xpLevel = it[Regulars.level]
                }
        }

        return xpLevel!!
    }

    fun getLastChecked(id: String): String? {
        var lastTimeChecked: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq id }
                .forEach {
                    lastTimeChecked = it[Regulars.lastCheckedDate]
                }
        }

        return lastTimeChecked
    }

    fun delete(id: String) {
        transaction {
            Regulars.deleteWhere { Regulars.id eq id }
        }
    }

    fun exists(id: String): Boolean {
        var exists = false
        transaction {
            exists = !Regulars.selectAll()
                .where { Regulars.id eq id }.empty()
        }

        return exists
    }

    fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = Regulars.selectAll().count()
        }

        return count
    }
}