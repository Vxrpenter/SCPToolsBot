package dev.vxrp.bot.database.queue;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueueManager {
    // This manager is linked to the sqllite manager because it is using the sqlite database

    public QueueManager(Connection connection) throws SQLException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(QueueManager.class);

        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS actionqueue(" +
                            "count INT PRIMARY KEY," +
                            "command TEXT NOT NULL," +
                            "time_added TEXT NOT NULL" +
                            ");"
            );
            logger.debug("Set up table {} with rows: {}, {}, {}",
                    ColorTool.apply(DCColor.GOLD, "actionqueue"),
                    ColorTool.apply(DCColor.RED, "count"),
                    ColorTool.apply(DCColor.RED, "command"),
                    ColorTool.apply(DCColor.GREEN, "time_added"));
            ScpTools.getLoggerManager().databaseLog(
                    "CREATE TABLE IF NOT EXISTS actionqueue(count INT PRIMARY KEY, command TEXT NOT NULL, time_added TEXT NOT NULL);",
                    "Created Table with all rows",
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }
}
