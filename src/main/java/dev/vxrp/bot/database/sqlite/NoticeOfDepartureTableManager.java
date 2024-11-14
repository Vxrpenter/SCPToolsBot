package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.records.noticeOfDeparture.NoticeOfDeparture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeOfDepartureTableManager {
    private final Connection connection;
    private final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    private final Logger logger = LoggerFactory.getLogger(NoticeOfDepartureTableManager.class);

    public NoticeOfDepartureTableManager(Connection connection) {
        this.connection = connection;
    }

    public void addNoticeOfDeparture(String id, String channel_message_id, String start_time, String end_time) throws SQLException, InterruptedException {
        if (exists(id)) {
            logger.warn("Notice of Departure already exists in Sqlite database... opting for deletion");
            deleteNoticeOfDeparture(id);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO notice_of_departure VALUES (?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, channel_message_id);
            statement.setString(3, start_time);
            statement.setString(4, end_time);
            statement.executeUpdate();
            logger.debug("Added notice of departure - id: {}, channel_message_id: {} , start_time: {} , end_time: {}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GREEN, channel_message_id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
            ScpTools.getLoggerManager().databaseLog(
                    "INSERT INTO notice_of_departure VALUES (?, ?, ?, ?)",
                    "Created new notice of departure with value id: "+ColorTool.apply(DCColor.GREEN, id)+
                            ", channel_message_id: "+ColorTool.apply(DCColor.GREEN, channel_message_id)+
                            ", start_time: "+ColorTool.apply(DCColor.GOLD, start_time)+
                            ", end_time: "+ColorTool.apply(DCColor.GOLD, end_time),
                    config.database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void updateNoticeOfDeparture(String id, String channel_message_id, String start_time, String end_time) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update notice of departure with id: {}. Id does not exist",
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=?, end_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, end_time);
            statement.setString(3, id);
            statement.executeUpdate();
            logger.debug("Updated notice of departure - id: {} , start_time: {} , end_time: {}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void updateEndTime(String id,String end_time) throws SQLException {
        if (!exists(id)) {
            logger.error("Failed to update notice of departure end time with id: {}. Id does not exist",
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET end_time=? WHERE id=?")) {
            statement.setString(1, end_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.debug("Updated end_time - id: {} , end_time: {}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void deleteNoticeOfDeparture(String id) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.error("Failed to delete notice of departure with id: {}. Id does not exist",
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.debug("Deleted notice of departure - id: {}",
                    ColorTool.apply(DCColor.RED, id));
            ScpTools.getLoggerManager().databaseLog(
                    "DELETE FROM notice_of_departure WHERE id=?",
                    "Deleted notice of departure id: "+ColorTool.apply(DCColor.GREEN, id),
                    config.database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public List<NoticeOfDeparture> getEveryNoticeOfDeparture() throws SQLException, InterruptedException {
        List<NoticeOfDeparture> noticeOfDepartureList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM notice_of_departure")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                ScpTools.getLoggerManager().databaseLog(
                        "SELECT * FROM notice_of_departure",
                        "Selected every notice of departure",
                        config.database_logging_channel_id(),
                        Color.ORANGE);
                while (resultSet.next()) {
                    noticeOfDepartureList.add(new NoticeOfDeparture(
                            resultSet.getString("id"),
                            resultSet.getString("channel_message_id").split(":")[0],
                            resultSet.getString("channel_message_id").split(":")[1],
                            resultSet.getString("start_time"),
                            resultSet.getString("end_time")));
                }
            }
        }
        return noticeOfDepartureList;
    }

    public NoticeOfDeparture getNoticeOfDeparture(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return new NoticeOfDeparture(
                        resultSet.getString("id"),
                        resultSet.getString("channel_message_id").split(":")[0],
                        resultSet.getString("channel_message_id").split(":")[1],
                        resultSet.getString("start_time"),
                        resultSet.getString("end_time"));
            }
        }
    }

    public boolean exists(String id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM notice_of_departure WHERE id=?);")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}