package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.records.NoticeOfDeparture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeOfDepartureTableManager {
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(NoticeOfDepartureTableManager.class);
    private final String prefix = ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite"));

    public NoticeOfDepartureTableManager(Connection connection) {
        this.connection = connection;
    }

    public void addNoticeOfDeparture(String id, String channel_message_id, String start_time, String end_time) throws SQLException, InterruptedException {
        if (exists(id)) {
            logger.warn("{} - Notice of Departure already exists in Sqlite database... opting for deletion", prefix);
            deleteNoticeOfDeparture(id);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO notice_of_departure VALUES (?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, channel_message_id);
            statement.setString(3, start_time);
            statement.setString(4, end_time);
            statement.executeUpdate();
            logger.info("{} - Added notice of departure - id: {}, channel_message_id: {} , start_time: {} , end_time: {}", prefix,
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
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void updateNoticeOfDeparture(String id, String channel_message_id, String start_time, String end_time) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update notice of departure with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=?, end_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, end_time);
            statement.setString(3, id);
            statement.executeUpdate();
            logger.info("{} - Updated notice of departure - id: {} , start_time: {} , end_time: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void updateChannelMessageId(String id, String channel_message_id) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update notice of departure channel and message id with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=? WHERE id=?")) {
            statement.setString(1, channel_message_id);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated start_time - id: {} , channel_message_id: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, channel_message_id));
        }
    }

    public void updateStartTime(String id,String start_time) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update notice of departure start time with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated start_time - id: {} , start_time: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time));
        }
    }

    public void updateEndTime(String id,String end_time) throws SQLException {
        if (!exists(id)) {
            logger.error("{} - Failed to update notice of departure end time with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET end_time=? WHERE id=?")) {
            statement.setString(1, end_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated end_time - id: {} , end_time: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void deleteNoticeOfDeparture(String id) throws SQLException, InterruptedException {
        if (!exists(id)) {
            logger.error("{} - Failed to delete notice of departure with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.info("{} - Deleted notice of departure - id: {}", prefix,
                    ColorTool.apply(DCColor.RED, id));
            ScpTools.getLoggerManager().databaseLog(
                    "DELETE FROM notice_of_departure WHERE id=?",
                    "Deleted notice of departure id: "+ColorTool.apply(DCColor.GREEN, id),
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
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
                        LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
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

    public String getStartTime(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT start_time FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.getString("start_time");
            }
        }
    }

    public String getEndTime(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT end_time FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.getString("end_time");
            }
        }
    }

    public String getChannelMessageId(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT FROM channel_message_id WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.getString("channel_message_id");
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