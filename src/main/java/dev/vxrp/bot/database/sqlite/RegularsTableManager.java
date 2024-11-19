package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor_DEPRECATED;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.records.regular.RegularMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegularsTableManager {
    private final Connection connection;
    private final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    private final Logger logger = LoggerFactory.getLogger(RegularsTableManager.class);

    public RegularsTableManager(Connection connection) {
        this.connection = connection;
    }

    public void addRegular(String id, String user_name, String group_role, String role, int time, String time_last_checked) throws SQLException, InterruptedException {
        if (exists(id)) {
            logger.warn("Regular already exists in Sqlite database... opting for deletion");
            deleteRegular(id);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO regulars VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, user_name);
            statement.setString(3, group_role);
            statement.setString(4, role);
            statement.setInt(5, time);
            statement.setString(6, time_last_checked);
            statement.setBoolean(7, false);
            statement.executeUpdate();
            logger.debug("Added regular - id: {}, user_name {}, group_role: {} , role: {} , time: {}, time_last_checked: {}, deactivated: {}",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, user_name),
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, group_role),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, role),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, String.valueOf(time)),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, time_last_checked),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, String.valueOf(false)));
            ScpTools.getLoggerManager().databaseLog(
                    "INSERT INTO regular VALUES (?, ?, ?, ?, ?, ?, ?)",
                    "Created new regular with value id: "+ColorTool.apply(DCColor_DEPRECATED.GREEN, id)+
                            ", user_name: "+ColorTool.apply(DCColor_DEPRECATED.GREEN, user_name)+
                            ", group_role: "+ColorTool.apply(DCColor_DEPRECATED.GREEN, group_role)+
                            ", role: "+ColorTool.apply(DCColor_DEPRECATED.GOLD, role)+
                            ", time: "+ColorTool.apply(DCColor_DEPRECATED.GOLD, time+
                            ", time_last_checked: "+ColorTool.apply(DCColor_DEPRECATED.GOLD, time_last_checked))+
                            ", deactivated: "+ColorTool.apply(DCColor_DEPRECATED.GOLD, String.valueOf(false)),
                    config.database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void updateGroupRole(String id, String group_role) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update regular group_role with id: {}. Id does not exist",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regulars SET group_role=? WHERE id=?")) {
            statement.setString(1, group_role);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("Updated group_role - id: {} , group_role: {}",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, group_role));
        }
    }

    public void updateRole(String id, String role) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update regular role with id: {}. Id does not exist",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regulars SET role=? WHERE id=?")) {
            statement.setString(1, role);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("Updated role - id: {} , role: {}",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, role));
        }
    }

    public void updateTime(String id, double time) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update regular time with id: {}. Id does not exist",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regulars SET time=? WHERE id=?")) {
            statement.setDouble(1, time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("Updated time - id: {} , time: {}",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, String.valueOf(time)));
        }
    }

    public void updateTimeLastChecked(String id, String time_last_checked) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update regular time_last_checked with id: {}. Id does not exist",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regulars SET time_last_checked=? WHERE id=?")) {
            statement.setString(1, time_last_checked);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("Updated time_last_checked - id: {} , time: {}",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, time_last_checked));
        }
    }

    public void updateDeactivated(String id, boolean deactivated) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update regular deactivated with id: {}. Id does not exist",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE regulars SET deactivated=? WHERE id=?")) {
            statement.setBoolean(1, deactivated);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("Updated deactivated - id: {} , time: {}",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    ColorTool.apply(DCColor_DEPRECATED.GOLD, String.valueOf(deactivated)));
        }
    }

    public List<RegularMember> getEveryRegularMember() throws SQLException {
        List<RegularMember> regularMembers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM regulars")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    regularMembers.add(new RegularMember(
                            resultSet.getString("id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("group_role"),
                            resultSet.getString("role"),
                            resultSet.getDouble("time"),
                            resultSet.getString("time_last_checked"),
                            resultSet.getBoolean("deactivated")));
                }
            }
        }
        return regularMembers;
    }

    public RegularMember getRegularMember(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM regulars WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return new RegularMember(
                        id,
                        resultSet.getString("user_name"),
                        resultSet.getString("group_role"),
                        resultSet.getString("role"),
                        resultSet.getDouble("time"),
                        resultSet.getString("time_last_checked"),
                        resultSet.getBoolean("deactivated")
                );
            }
        }
    }

    public void deleteRegular(String id) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.error("Failed to delete regular with id: {}. Id does not exist",
                    ColorTool.apply(DCColor_DEPRECATED.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM regulars WHERE id=?")) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.debug("Deleted regular - id: {}",
                    ColorTool.apply(DCColor_DEPRECATED.RED, id));
            ScpTools.getLoggerManager().databaseLog(
                    "DELETE FROM regulars WHERE id=?",
                    "Deleted regular id: "+ColorTool.apply(DCColor_DEPRECATED.GREEN, id),
                    config.database_logging_channel_id(),
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
