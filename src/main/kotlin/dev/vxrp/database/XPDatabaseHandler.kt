package dev.vxrp.database

import dev.vxrp.configuration.loaders.Config
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

class XPDatabaseHandler(val config: Config) {
    var database: Database? = null
    private val logger = LoggerFactory.getLogger(XPDatabaseHandler::class.java)

    fun connectToDatabase(): Database? {
        if (config.settings.xp.active) return null

        if (config.settings.database.dataUsePredefined == "NONE" && config.settings.database.customUrl == config.settings.xp.databaseAddress)  {
            logger.warn("Found Custom database and Xp database to be the same. Cancelling connection to xp database")
            return null
        }

        val url = "jdbc:mysql://${config.settings.xp.databaseAddress}"

        return Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", config.settings.xp.databaseUser, config.settings.xp.databasePassword)
    }
}