package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table

class ApplicationTypeTable {
    object ApplicationTypes : Table("applications") {
        val name = text("name")
        val active = bool("active").default(false)
        val initializer = text("initializer")

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(name)
    }
}