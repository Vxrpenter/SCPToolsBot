package dev.vxrp.database

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.sqlite.tables.*
import dev.vxrp.database.tables.*
import dev.vxrp.util.enums.Databasetype
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class DatabaseManager(val config: Config, folder: String, val file: String) {
    private val dir = System.getProperty("user.dir")

    init {
        File("$dir/$folder/").also { if (!it.exists()) it.mkdirs() }

        File("$dir/$folder/$file").also { if (!it.exists()) it.createNewFile() }

        if (config.settings.database.dataUsePredefined == "SQLITE") {
            Database.connect("jdbc:sqlite:$dir/$folder/$file", driver = "org.sqlite.JDBC")
            createTables()
        }
        if (config.settings.database.dataUsePredefined == "NONE") {
            when (Databasetype.SQlITE.takeIf { !enumContains<Databasetype>(config.settings.database.customType) }
                ?: Databasetype.valueOf(config.settings.database.customType)) {
                Databasetype.SQlITE -> {
                    val url = "jdbc:sqlite:${config.settings.database.customUrl}"

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
            SchemaUtils.create(TicketTable.Tickets)
            SchemaUtils.create(NoticeOfDepartureTable.NoticeOfDepartures)
            SchemaUtils.create(RegularsTable.Regulars)
            SchemaUtils.create(ActionQueueTable.ActionQueue)
            SchemaUtils.create(StatusTable.Status)
            SchemaUtils.create(ConnectionTable.Connections)
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}