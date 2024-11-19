package dev.vxrp.database.sqlite.tables

import dev.vxrp.util.Enums.DCColor_DEPRECATED
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
                dev.vxrp.util.colors.ColorTool.apply(DCColor_DEPRECATED.GOLD, "tickets"),
                dev.vxrp.util.colors.ColorTool.apply(DCColor_DEPRECATED.RED, "id"),
                dev.vxrp.util.colors.ColorTool.apply(DCColor_DEPRECATED.RED, "identifier"),
                dev.vxrp.util.colors.ColorTool.apply(DCColor_DEPRECATED.GREEN, "creation_data"),
                dev.vxrp.util.colors.ColorTool.apply(DCColor_DEPRECATED.GREEN, "creator"),
                dev.vxrp.util.colors.ColorTool.apply(DCColor_DEPRECATED.GREEN, "handler")
            )
    }}
}