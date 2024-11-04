package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegularsTableManager {
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(RegularsTableManager.class);
    private final String prefix = ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite"));

    public RegularsTableManager(Connection connection) {
        this.connection = connection;
    }

    public void addRegular(String id, String group_role, String role, int time, String time_last_checked) throws SQLException, InterruptedException {
        if (exists(id)) {
            logger.warn("{} - Regular already exists in Sqlite database... opting for deletion", prefix);
            deleteRegular(id);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO regulars VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, group_role);
            statement.setString(3, role);
            statement.setInt(4, time);
            statement.setString(5, time_last_checked);
            statement.executeUpdate();
            logger.debug("{} - Added regular - id: {}, group_role: {} , role: {} , time: {}, time_last_checked: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GREEN, group_role),
                    ColorTool.apply(DCColor.GOLD, role),
                    ColorTool.apply(DCColor.GOLD, String.valueOf(time)),
                    ColorTool.apply(DCColor.GOLD, time_last_checked));
            ScpTools.getLoggerManager().databaseLog(
                    "INSERT INTO regular VALUES (?, ?, ?, ?, ?)",
                    "Created new regular with value id: "+ColorTool.apply(DCColor.GREEN, id)+
                            ", group_role: "+ColorTool.apply(DCColor.GREEN, group_role)+
                            ", role: "+ColorTool.apply(DCColor.GOLD, role)+
                            ", time: "+ColorTool.apply(DCColor.GOLD, time+
                            ", time_last_checked: "+ColorTool.apply(DCColor.GOLD, time_last_checked)),
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void updateGroupRole(String id, String group_role) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update regular group_role with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regular SET group_role=? WHERE id=?")) {
            statement.setString(1, group_role);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("{} - Updated group_role - id: {} , group_role: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, group_role));
        }
    }

    public void updateRole(String id, String role) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update regular role with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regular SET role=? WHERE id=?")) {
            statement.setString(1, role);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("{} - Updated role - id: {} , role: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, role));
        }
    }

    public void updateTime(String id, int time) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update regular time with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regular SET time=? WHERE id=?")) {
            statement.setString(1, String.valueOf(time));
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("{} - Updated time - id: {} , time: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, String.valueOf(time)));
        }
    }

    public void updateTimeLastChecked(String id, String time_last_checked) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update regular time_last_checked with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regular SET time_last_checked=? WHERE id=?")) {
            statement.setString(1, time_last_checked);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("{} - Updated time_last_checked - id: {} , time: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, time_last_checked));
        }
    }

    public void deleteRegular(String id) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.error("{} - Failed to delete regular with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM regulars WHERE id=?")) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.debug("{} - Deleted regular - id: {}", prefix,
                    ColorTool.apply(DCColor.RED, id));
            ScpTools.getLoggerManager().databaseLog(
                    "DELETE FROM regulars WHERE id=?",
                    "Deleted regular id: "+ColorTool.apply(DCColor.GREEN, id),
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public boolean exists(String id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM regulars WHERE id=?);")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
