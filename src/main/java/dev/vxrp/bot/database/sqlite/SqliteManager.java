package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteManager {
    private final TicketsTableManager ticketsTableManager;
    private final NoticeOfDepartureTableManager noticeOfDepartureTableManager;
    private final RegularsTableManager regularsTableManager;

    public SqliteManager(String path) throws SQLException, InterruptedException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        ticketsTableManager = new TicketsTableManager(connection);
        noticeOfDepartureTableManager = new NoticeOfDepartureTableManager(connection);
        regularsTableManager = new RegularsTableManager(connection);
        Logger logger = LoggerFactory.getLogger(SqliteManager.class);
        String prefix = ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite"));
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS tickets (" +
                            "id NOT NULL," +
                            "identifier TEXT NOT NULL," +
                            "creation_date TEXT NOT NULL," +
                            "creatorId TEXT NOT NULL," +
                            "handlerId TEXT," +
                            "PRIMARY KEY ( id, identifier ));");
            logger.debug("{} - Set up table {} with rows: {}, {}, {}, {}, {}", prefix,
                    ColorTool.apply(DCColor.GOLD, "tickets"),
                    ColorTool.apply(DCColor.RED, "id"),
                    ColorTool.apply(DCColor.RED, "identifier"),
                    ColorTool.apply(DCColor.GREEN, "creation_data"),
                    ColorTool.apply(DCColor.GREEN, "creator"),
                    ColorTool.apply(DCColor.GREEN, "handler"));
            ScpTools.getLoggerManager().databaseLog(
                    "CREATE TABLE IF NOT EXISTS tickets (id NOT NULL, identifier TEXT NOT NULL, creation_date TEXT NOT NULL, creatorId TEXT NOT NULL, handlerId TEXT, PRIMARY KEY ( id, identifier ));",
                    "Created Table with all rows",
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS notice_of_departure (" +
                            "id TEXT PRIMARY KEY," +
                            "channel_message_id TEXT NOT NULL," +
                            "start_time TEXT NOT NULL," +
                            "end_time TEXT NOT NULL" +
                            ");");
            logger.debug("{} - Set up table {} with rows: {}, {}, {}, {}", prefix,
                    ColorTool.apply(DCColor.GOLD, "notice_of_departure"),
                    ColorTool.apply(DCColor.RED, "id"),
                    ColorTool.apply(DCColor.GREEN, "channel_message_id"),
                    ColorTool.apply(DCColor.GREEN, "start_time"),
                    ColorTool.apply(DCColor.GREEN, "end_time"));

            ScpTools.getLoggerManager().databaseLog(
                    "CREATE TABLE IF NOT EXISTS notice_of_departure (id TEXT PRIMARY KEY, channel_message_id TEXT NOT NULL, start_time TEXT NOT NULL, end_time TEXT NOT NULL);",
                    "Created Table with all rows",
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS regulars (" +
                            "id TEXT PRIMARY KEY," +
                            "group_role TEXT," +
                            "role TEXT NOT NULL," +
                            "time INT NOT NULL" +
                            "time_last_checked TEXT NOT NULL" +
                            ");"
            );
            logger.debug("{} - Set up table {} with rows: {}, {}, {}, {}, {}", prefix,
                    ColorTool.apply(DCColor.GOLD, "regulars"),
                    ColorTool.apply(DCColor.RED, "id"),
                    ColorTool.apply(DCColor.GREEN, "group_role"),
                    ColorTool.apply(DCColor.GREEN, "role"),
                    ColorTool.apply(DCColor.GREEN, "time"),
                    ColorTool.apply(DCColor.GREEN, "time_last_checked"));

            ScpTools.getLoggerManager().databaseLog(
                    "CREATE TABLE IF NOT EXISTS regulars (id TEXT PRIMARY KEY, group_role TEXT NOT NULL, role TEXT NOT NULL, time INT NOT NULL, time_last_checked TEXT NOT NULL);",
                    "Created Table with all rows",
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
        logger.info("Set up Sqlite database correctly");
    }

    public TicketsTableManager getTicketsTableManager() {
        return ticketsTableManager;
    }
    public NoticeOfDepartureTableManager getNoticeOfDepartureTableManager() {
        return noticeOfDepartureTableManager;
    }
    public RegularsTableManager getRegularsTableManager() {
        return regularsTableManager;
    }
}
