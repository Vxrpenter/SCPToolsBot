package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class RegularsTable {
    object Regulars : Table("regulars") {
        val id = text("id")
        val active = bool("active").default(true)
        val groupRoleId = text("group_id")
        val roleId = text("role_id")
        val playtime = double("playtime")
        val lastCheckedDate = text("last_checked_date")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    fun addToDatabase(userId: String, activeRegular: Boolean, groupRoleIdentification: String, roleIdentification: String, time: Double, lastChecked: String) {
        if (!exists(userId)) return

        transaction {
            Regulars.insert {
                it[id] = userId
                it[active] = activeRegular
                it[groupRoleId] = groupRoleIdentification
                it[roleId] = roleIdentification
                it[lastCheckedDate] = lastChecked
                it[playtime] = time
            }
        }
    }

    fun getActive(userId: String): Boolean? {
        var active: Boolean? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    active = it[Regulars.active]
                }
        }

        return active
    }

    fun getGroup(userId: String): String? {
        var group: String? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    group = it[Regulars.groupRoleId]
                }
        }

        return group
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

    fun getPlaytime(userId: String): Double? {
        var playtime: Double? = null

        transaction {
            Regulars.selectAll()
                .where { Regulars.id eq userId }
                .forEach {
                    playtime = it[Regulars.playtime]
                }
        }

        return playtime
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