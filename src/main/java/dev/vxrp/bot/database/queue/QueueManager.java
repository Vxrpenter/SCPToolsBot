package dev.vxrp.bot.database.queue;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.records.actionQueue.ActionQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueueManager {
    // This manager is linked to the sqllite manager because it is using the sqlite database
    private final Logger logger = LoggerFactory.getLogger(QueueManager.class);
    private final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    private final Connection connection;

    public QueueManager(Connection connection) throws SQLException, InterruptedException {
        this.connection = connection;

        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS actionqueue(" +
                            "id TEXT PRIMARY KEY," +
                            "command TEXT NOT NULL," +
                            "time_added TEXT NOT NULL," +
                            "processed BOOLEAN NOT NULL" +
                            ");"
            );
            logger.debug("Set up table {} with rows: {}, {}, {}, {}",
                    ColorTool.apply(DCColor.GOLD, "actionqueue"),
                    ColorTool.apply(DCColor.RED, "id"),
                    ColorTool.apply(DCColor.RED, "command"),
                    ColorTool.apply(DCColor.GREEN, "time_added"),
                    ColorTool.apply(DCColor.GREEN, "processed"));
            ScpTools.getLoggerManager().databaseLog(
                    "CREATE TABLE IF NOT EXISTS actionqueue(id TEXT PRIMARY KEY, command TEXT NOT NULL, time_added TEXT NOT NULL, processed BOOLEAN NOT NULL);",
                    "Created Table with all rows",
                    config.database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void addAction(String id, String command, String timeAdded) throws SQLException, InterruptedException {
        if (exists(id)) {
            logger.debug("Action {} already exists", id);
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO actionqueue VALUES (?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, command);
            statement.setString(3, timeAdded);
            statement.setBoolean(4, false);
            statement.executeUpdate();
            logger.debug("Added action to queue - id: {}, command: {} , time_added: {}, processed {}",
                    ColorTool.apply(DCColor.RED, id),
                    ColorTool.apply(DCColor.GREEN, command),
                    ColorTool.apply(DCColor.GREEN, timeAdded),
                    ColorTool.apply(DCColor.GREEN, "false"));
            ScpTools.getLoggerManager().databaseLog(
                    "INSERT INTO actionqueue VALUES (?, ?, ?, ?)",
                    "Created new action queue entry with value count: "+ColorTool.apply(DCColor.GREEN, id)+
                            ", command: "+ColorTool.apply(DCColor.GREEN, command)+
                            ", timeAdded: "+ColorTool.apply(DCColor.GOLD, timeAdded)+
                            ", processed: false",
                    config.database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void updateProcessed(String id, boolean processed) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.debug("Action {} doesn't exists", id);
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE actionqueue SET processed = ? WHERE id = ?")) {
            statement.setBoolean(1, processed);
            statement.setString(2, id);
            statement.executeUpdate();
            ScpTools.getLoggerManager().databaseLog(
                    "UPDATE actionqueue SET processed = ? WHERE id = ?",
                    "Updated action queue with id: "+ColorTool.apply(DCColor.RED, id)+
                    ", processed: "+ColorTool.apply(DCColor.GREEN, String.valueOf(processed)),
                    config.database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public List<ActionQueue> getEveryActionQueue() throws SQLException {
        List<ActionQueue> actionQueues = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM actionqueue")) {
            try (ResultSet resultSet = statement.executeQuery()) {


                while (resultSet.next()) {
                    actionQueues.add(new ActionQueue(
                            resultSet.getString("id"),
                            resultSet.getString("command"),
                            resultSet.getString("time_added"),
                            resultSet.getBoolean("processed")
                    ));
                }
            }
        }
        return actionQueues;
    }

    public boolean getProcessed(String id) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.debug("Action {} doesn't exists", id);
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT processed FROM actionqueue WHERE id = ?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                ScpTools.getLoggerManager().databaseLog(
                        "SELECT processed FROM actionqueue WHERE id = ?",
                        "Selected processed from id : "+ColorTool.apply(DCColor.RED, id)+
                                ", processed: "+ColorTool.apply(DCColor.GREEN, String.valueOf(resultSet.getBoolean("processed"))),
                                config.database_logging_channel_id(),
                                Color.ORANGE);
                return resultSet.getBoolean("processed");
            }
        }
    }

    public String getCommand(String id) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.debug("Action {} doesn't exists", id);
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT command FROM actionqueue WHERE id = ?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                ScpTools.getLoggerManager().databaseLog(
                        "SELECT command FROM actionqueue WHERE id = ?",
                        "Selected command from id : "+ColorTool.apply(DCColor.RED, id)+
                                ", command: "+ColorTool.apply(DCColor.GREEN, resultSet.getString("command")),
                        config.database_logging_channel_id(),
                        Color.ORANGE);
                return resultSet.getString("command");
            }
        }
    }

    public void deleteAction(String id) throws SQLException, InterruptedException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM actionqueue WHERE id = ?")) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.debug("Deleted action from queue - id: {}", id);
            ScpTools.getLoggerManager().databaseLog("DELETE FROM actionqueue WHERE id = ?",
                    "Deleted action from queue",
            config.database_logging_channel_id(),
            Color.ORANGE);
        }
    }

    public boolean exists(String id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM actionqueue WHERE id=?);")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
