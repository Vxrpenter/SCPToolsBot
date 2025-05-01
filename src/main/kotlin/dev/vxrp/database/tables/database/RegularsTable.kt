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
        val lastCheckedDate = text("last_checked_date")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(userId: String, activeRegular: Boolean, roleGroup: String, groupRoleIdentification: String?, roleIdentification: String, time: Double, lastChecked: String, xpLevel: Int) {
        if (exists(userId)) return

        transaction {
            Regulars.insert {
                it[id] = userId
                it[active] = activeRegular
                it[group] = roleGroup
                it[groupRoleId] = groupRoleIdentification
                it[roleId] = roleIdentification
                it[playtime] = time
                it[level] = xpLevel
                it[lastCheckedDate] = lastChecked
            }
        }
    }

    fun getAllEntrys(): List<RegularDatabaseEntry> {
        val list = mutableListOf<RegularDatabaseEntry>()

        transaction {
            Regulars.selectAll()
                .forEach {
                    list.add(
                        RegularDatabaseEntry(
                            it[Regulars.id],
                            it[Regulars.active],
                            it[Regulars.group],
                            it[Regulars.groupRoleId],
                            it[Regulars.roleId],
                            it[Regulars.lastCheckedDate],
                            it[Regulars.playtime]
                        )
                    )
                }
        }

        return list
    }

    fun setActive(userId: String, value: Boolean) {
        transaction {
            Regulars.update({ Regulars.id eq userId }) {
                it[active] = value
            }
        }
    }

    fun setPlaytime(userId: String, time: Double) {
        transaction {
            Regulars.update({ Regulars.id eq userId }) {
                it[playtime] = time
            }
        }
    }

    fun setLevel(userId: String, xpLevel: Int) {
        transaction {
            Regulars.update({ Regulars.id eq userId }) {
                it[level] = xpLevel
            }
        }
    }

    fun setLastCheckedDate(userId: String, timestamp: String) {
        transaction {
            Regulars.update({ Regulars.id eq userId }) {
                it[lastCheckedDate] = timestamp
            }
        }
    }

    fun getActive(userId: String): Boolean {
        var active: Boolean? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    active = it[Regulars.active]
                }
        }

        return active!!
    }

    fun getGroupRole(userId: String): String? {
        var groupRole: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    groupRole = it[Regulars.groupRoleId]
                }
        }

        return groupRole
    }

    fun getGroup(userId: String): String {
        var groupName: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    groupName = it[Regulars.group]
                }
        }

        return groupName!!
    }

    fun getRole(userId: String): String? {
        var role: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    role = it[Regulars.roleId]
                }
        }

        return role
    }

    fun getPlaytime(userId: String): Double {
        var playtime: Double? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    playtime = it[Regulars.playtime]
                }
        }

        return playtime!!
    }

    fun getLevel(userId: String): Int {
        var xpLevel: Int? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    xpLevel = it[Regulars.level]
                }
        }

        return xpLevel!!
    }

    fun getLastChecked(userId: String): String? {
        var lastTimeChecked: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    lastTimeChecked = it[Regulars.lastCheckedDate]
                }
        }

        return lastTimeChecked
    }

    fun delete(userId: String) {
        transaction {
            Regulars.deleteWhere { id eq userId }
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
}