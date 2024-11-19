package dev.vxrp.database.sqlite.tables

import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.enums.DCColor
import org.slf4j.LoggerFactory
import java.sql.Connection

class TicketTableManager(connection: Connection) {
    private val logger = LoggerFactory.getLogger(TicketTableManager::class.java)

    init {
        connection.createStatement().use { statement -> statement.execute("CREATE TABLE IF NOT EXISTS ticket(" +
                "id TEXT NOT NULL," +
                "identifier TEXT NOT NULL," +
                "creation_date TEXT NOT NULL," +
                "creatorId TEXT NOT NULL," +
                "handlerId TEXT," +
                "PRIMARY KEY ( id, identifier ));")

            logger.debug(
                "Set up table {} with rows: {}, {}, {}, {}, {}",
                ColorTool().apply(DCColor.GOLD, "tickets"),
                ColorTool().apply(DCColor.RED, "id"),
                ColorTool().apply(DCColor.RED, "identifier"),
                ColorTool().apply(DCColor.GREEN, "creation_data"),
                ColorTool().apply(DCColor.GREEN, "creator"),
                ColorTool().apply(DCColor.GREEN, "handler")
            )
    }}
}