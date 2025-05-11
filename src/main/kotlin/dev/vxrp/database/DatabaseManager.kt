package dev.vxrp.database

import dev.vxrp.configuration.data.Config
import dev.vxrp.database.enums.DatabaseType
import dev.vxrp.database.tables.database.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.io.File
import java.sql.SQLException
import kotlin.system.exitProcess

class DatabaseManager(val config: Config, private val folder: String, val file: String) {
    private val dir = System.getProperty("user.dir")
    private val logger = LoggerFactory.getLogger(DatabaseManager::class.java)

    init {
        val database = connectToDatabase()

        if (database == null) {
            logger.error("Failed to connect to default database, exiting...")
            exitProcess(1)
        } else {
            logger.info("Connection to default database fully established")

            if (config.settings.xp.active) connectToXPDatabase(database)
            TransactionManager.defaultDatabase = database

            createTables(database)
        }
    }

    private fun connectToXPDatabase(database: Database) {
        val xpDatabase = XPDatabaseHandler(config).connectToDatabase()

        if (xpDatabase == null) {
            XPDatabaseHandler(config).database = database
        } else {
            XPDatabaseHandler(config).database = xpDatabase
        }
    }

    private fun connectToDatabase(): Database? {
        if (config.settings.database.dataUsePredefined == "SQLITE") {
            File("$dir/$folder/").also { if (!it.exists()) it.mkdirs() }
            File("$dir/$folder/$file").also { if (!it.exists()) it.createNewFile() }

            return try {
                Database.connect("jdbc:sqlite:$dir/$folder/$file", driver = "org.sqlite.JDBC")
            } catch (_: SQLException) {
                logger.error("Could not connect to default sqlite database")
                null
            }
        }

        when (DatabaseType.SQlITE.takeIf { !enumContains<DatabaseType>(config.settings.database.customType) }
            ?: DatabaseType.valueOf(config.settings.database.customType)) {
            DatabaseType.SQlITE -> {
                val url = "jdbc:sqlite://${config.settings.database.customUrl}"

                return try {
                    Database.connect(url, driver = "org.sqlite.JDBC", config.settings.database.customUsername, config.settings.database.customPassword)
                } catch (_: SQLException) {
                    logger.error("Could not connect to sqlite database: $url")
                    null
                }
            }

            DatabaseType.MYSQL -> {
                val url = "jdbc:mysql://${config.settings.database.customUrl}"

                return try {
                    Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
                } catch (_: SQLException) {
                    logger.error("Could not connect to mysql database: $url")
                    null
                }
            }

            DatabaseType.POSTGRESQL -> {
                val url = "jdbc:postgresql://${config.settings.database.customUrl}"

                return try {
                    Database.connect(url, driver = "org.postgresql.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
                } catch (_: SQLException) {
                    logger.error("Could not connect to postgresql database: $url")
                    null
                }
            }

            DatabaseType.MARiADB -> {
                val url = "jdbc:mariadb://${config.settings.database.customUrl}"

                return try {
                    Database.connect(url, driver = "org.mariadb.jdbc.Driver", config.settings.database.customUsername, config.settings.database.customPassword)
                } catch (_: SQLException) {
                    logger.error("Could not connect to mariadb database: $url")
                    null
                }
            }
        }
    }

    private fun createTables(database: Database?) {
        transaction(database) {
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