package dev.vxrp.database.sqlite

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.tables.NoticeOfDeparture
import dev.vxrp.database.sqlite.tables.Regulars
import dev.vxrp.database.sqlite.tables.Ticket
import dev.vxrp.util.enums.DATABASE
import org.jetbrains.exposed.sql.*

import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class SqliteManager(val config: Config, folder: String, val file: String) {
    private val dir = System.getProperty("user.dir")

    init {
        File("$dir/$folder/").also { if (!it.exists()) it.mkdirs() }

        File("$dir/$folder/$file").also { if (!it.exists()) it.createNewFile() }

        if (config.database.dataUsePredefined == "SQLITE") {
            Database.connect("jdbc:sqlite:$dir/$folder/$file", driver = "org.sqlite.JDBC")
            createTables()
        }
        if (config.database.dataUsePredefined == "NONE") {
            when(DATABASE.valueOf(config.database.customType)) {
                DATABASE.SQlITE -> {
                    val url = "jdbc:sqlite:${config.database.customUrl}"

                    Database.connect(url, driver = "org.sqlite.JDBC")
                    createTables()
                }

                DATABASE.MYSQL -> {
                    // WIP
                }

                DATABASE.POSTGRESQL -> {
                    // WIP
                }

                DATABASE.MARiADB -> {
                    // WIP
                }
            }
        }
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(Ticket.Tickets)
            SchemaUtils.create(NoticeOfDeparture.NoticeOfDepartures)
            SchemaUtils.create(Regulars.Regulars)
        }
    }
}