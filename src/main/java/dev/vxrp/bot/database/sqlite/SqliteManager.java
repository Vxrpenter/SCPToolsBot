package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.objects.NoticeOfDeparture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SqliteManager {
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(SqliteManager.class);

    public SqliteManager(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS notice_of_departure (" +
                    "id TEXT PRIMARY KEY," +
                    "channel_message_id TEXT NOT NULL," +
                    "start_time TEXT NOT NULL," +
                    "end_time TEXT NOT NULL" +
                    ");");
            logger.info("{} - Set up table {} with rows: {}, {}, {}, {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.GOLD, "notice_of_departure"),
                    ColorTool.apply(DCColor.GREEN, "id"),
                    ColorTool.apply(DCColor.GREEN, "channel_message_id"),
                    ColorTool.apply(DCColor.GREEN, "start_time"),
                    ColorTool.apply(DCColor.GREEN, "end_time"));
        }
        logger.info("{} - Set up Sqlite database correctly",
                ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")));
    }

    public void addNoticeOfDeparture(String id, String channel_message_id, String start_time, String end_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO notice_of_departure VALUES (?,?,?,?)")) {
            statement.setString(1, id);
            statement.setString(2, channel_message_id);
            statement.setString(3, start_time);
            statement.setString(4, end_time);
            statement.executeUpdate();
            logger.info("{} - Added notice of departure - id: {}, channel_message_id: {} , start_time: {} , end_time: {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GREEN, channel_message_id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void updateNoticeOfDeparture(String id, String channel_message_id, String start_time, String end_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=?, end_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, end_time);
            statement.setString(3, id);
            statement.executeUpdate();
            logger.info("{} - Updated notice of departure - id: {} , start_time: {} , end_time: {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void updateChannelMessageId(String id, String channel_message_id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=? WHERE id=?")) {
            statement.setString(1, channel_message_id);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated start_time - id: {} , channel_message_id: {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, channel_message_id));
        }
    }

    public void updateStartTime(String id,String start_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated start_time - id: {} , start_time: {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time));
        }
    }

    public void updateEndTime(String id,String end_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET end_time=? WHERE id=?")) {
            statement.setString(1, end_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated end_time - id: {} , end_time: {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void deleteNoticeOfDeparture(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.info("{} - Deleted notice of departure - id: {}",
                    ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite")),
                    ColorTool.apply(DCColor.RED, id));
        }
    }

    public NoticeOfDeparture getNoticeOfDeparture(String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return new NoticeOfDeparture(resultSet.getString("channel_message_id"), resultSet.getString("start_time"), resultSet.getString("end_time"));
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
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM notice_of_departure WHERE id=?")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return true;
            } catch (SQLException e) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}