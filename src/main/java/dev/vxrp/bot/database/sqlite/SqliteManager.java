package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
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
                    "start_time TEXT NOT NULL," +
                    "end_time TEXT NOT NULL" +
                    ");");
        }
        logger.info("Set up Sqlite database correctly");
    }

    public void addNoticeOfDeparture(String id,String start_time, String end_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO notice_of_departure VALUES (?,?,?)")) {
            statement.setString(1, id);
            statement.setString(2, start_time);
            statement.setString(3, end_time);
            statement.executeUpdate();
            logger.info("Added notice of departure - id:{} , start_time:{} , end_time:{}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void updateNoticeOfDeparture(String id,String start_time, String end_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=?, end_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, end_time);
            statement.setString(3, id);
            statement.executeUpdate();
            logger.info("Updated notice of departure - id:{} , start_time:{} , end_time:{}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time),
                    ColorTool.apply(DCColor.GOLD, end_time));
        }
    }

    public void updateStartTime(String id,String start_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET start_time=? WHERE id=?")) {
            statement.setString(1, start_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("Updated start_time - id:{} , start_time:{}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, start_time));
        }
    }

    public void updateEndTime(String id,String end_time) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE notice_of_departure SET end_time=? WHERE id=?")) {
            statement.setString(1, end_time);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("Updated end_time - id:{} , end_time:{}",
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, end_time));
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