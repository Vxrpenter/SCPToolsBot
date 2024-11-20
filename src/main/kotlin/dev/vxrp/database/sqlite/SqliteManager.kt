package dev.vxrp.database.sqlite

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.tables.ActionQueue
import dev.vxrp.database.sqlite.tables.NoticeOfDeparture
import dev.vxrp.database.sqlite.tables.Regulars
import dev.vxrp.database.sqlite.tables.Ticket
import dev.vxrp.util.enums.Databasetype
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
            when(Databasetype.SQlITE.takeIf { !enumContains<Databasetype>(config.database.customType) } ?: Databasetype.valueOf(config.database.customType)) {
                Databasetype.SQlITE -> {
                    val url = "jdbc:sqlite:${config.database.customUrl}"

                    Database.connect(url, driver = "org.sqlite.JDBC")
                    createTables()
                }

                Databasetype.MYSQL -> {
                    // WIP
                }

                Databasetype.POSTGRESQL -> {
                    // WIP
                }

                Databasetype.MARiADB -> {
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
            SchemaUtils.create(ActionQueue.ActionQueue)
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name}
    }
}