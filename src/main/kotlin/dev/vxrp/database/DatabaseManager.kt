package dev.vxrp.database

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.tables.*
import dev.vxrp.database.enums.DatabaseType
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
            when (DatabaseType.SQlITE.takeIf { !enumContains<DatabaseType>(config.settings.database.customType) }
                ?: DatabaseType.valueOf(config.settings.database.customType)) {
                DatabaseType.SQlITE -> {
                    val url = "jdbc:sqlite:${config.settings.database.customUrl}"

                    Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customUsername)
                    createTables()
                }

                DatabaseType.MYSQL -> {
                    val url = "jdbc:mysql:${config.settings.database.customUrl}"

                    Database.connect(url, driver = "")
                    createTables()
                }

                DatabaseType.POSTGRESQL -> {
                    // WIP
                }

                DatabaseType.MARiADB -> {
                    val url = "jdbc:mariadb:${config.settings.database.customUrl}"

                    Database.connect(url, driver = "org.mariadb.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customUsername)
                    createTables()
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
            SchemaUtils.create(ApplicationTypeTable.ApplicationTypes)
            SchemaUtils.create(ApplicationTable.Applications)
            SchemaUtils.create(MessageTable.Messages)
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}