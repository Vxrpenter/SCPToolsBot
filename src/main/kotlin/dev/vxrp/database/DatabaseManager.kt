package dev.vxrp.database

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.database.enums.DatabaseType
import dev.vxrp.database.tables.*
import dev.vxrp.database.tables.database.ApplicationTable
import dev.vxrp.database.tables.database.ApplicationTypeTable
import dev.vxrp.database.tables.database.ConnectionTable
import dev.vxrp.database.tables.database.MessageTable
import dev.vxrp.database.tables.database.NoticeOfDepartureTable
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.database.tables.database.StatusTable
import dev.vxrp.database.tables.database.TicketTable
import dev.vxrp.database.tables.database.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class DatabaseManager(val config: Config, private val folder: String, val file: String) {
    private val dir = System.getProperty("user.dir")

    init {
        val database = connectToDatabase()
        val xpDatabase = XPDatabaseHandler(config).connectToDatabase()

        TransactionManager.defaultDatabase = database

        if (xpDatabase != null) XPDatabaseHandler(config).database = xpDatabase
        else XPDatabaseHandler(config).database = database

        createTables()
    }

    private fun connectToDatabase(): Database? {
        if (config.settings.database.dataUsePredefined == "SQLITE") {
            File("$dir/$folder/").also { if (!it.exists()) it.mkdirs() }
            File("$dir/$folder/$file").also { if (!it.exists()) it.createNewFile() }

            Database.connect("jdbc:sqlite:$dir/$folder/$file", driver = "org.sqlite.JDBC")
            return null
        }

        when (DatabaseType.SQlITE.takeIf { !enumContains<DatabaseType>(config.settings.database.customType) }
            ?: DatabaseType.valueOf(config.settings.database.customType)) {
            DatabaseType.SQlITE -> {
                val url = "jdbc:sqlite://${config.settings.database.customUrl}"

                return Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
            }

            DatabaseType.MYSQL -> {
                val url = "jdbc:mysql://${config.settings.database.customUrl}"

                return Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
            }

            DatabaseType.POSTGRESQL -> {
                val url = "jdbc:postgresql://${config.settings.database.customUrl}"

                return Database.connect(url, driver = "org.postgresql.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
            }

            DatabaseType.MARiADB -> {
                val url = "jdbc:mariadb://${config.settings.database.customUrl}"

                return Database.connect(url, driver = "org.mariadb.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
            }
        }
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(TicketTable.Tickets)
            SchemaUtils.create(NoticeOfDepartureTable.NoticeOfDepartures)
            SchemaUtils.create(RegularsTable.Regulars)
            SchemaUtils.create(StatusTable.Status)
            SchemaUtils.create(ConnectionTable.Connections)
            SchemaUtils.create(ApplicationTypeTable.ApplicationTypes)
            SchemaUtils.create(ApplicationTable.Applications)
            SchemaUtils.create(MessageTable.Messages)
            SchemaUtils.create(UserTable.Users)
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}