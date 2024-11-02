package dev.vxrp.bot.database.sqlite;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.Enums.TicketIdentifier;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.records.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketsTableManager {
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(TicketsTableManager.class);
    private final String prefix = ColorTool.apply(DCColor.GOLD, ColorTool.apply(DCColor.BOLD, "SQLite"));

    public TicketsTableManager(Connection connection) {
        this.connection = connection;
    }

    public void addTicket(String id, TicketIdentifier identifier, String creation_date, String creatorId, String handlerId) throws SQLException, InterruptedException {
        if (existsId(id)) {
            logger.warn("{} - Ticket already exists in Sqlite database... opting for deletion", prefix);
            deleteTicket(id);
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO tickets VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, identifier.toString());
            statement.setString(3, creation_date);
            statement.setString(4, creatorId);
            statement.setString(5, handlerId);
            statement.executeUpdate();
            logger.info("{} - Added ticket - id: {}, identifier: {} , creation_date: {} , creatorId: {} , handlerId: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GREEN, identifier.toString()),
                    ColorTool.apply(DCColor.GOLD, creation_date),
                    ColorTool.apply(DCColor.GOLD, creatorId),
                    ColorTool.apply(DCColor.GOLD, handlerId));
            System.out.println(statement.getResultSet());
            ScpTools.getLoggerManager().databaseLog(
                    "INSERT INTO tickets VALUES (?, ?, ?, ?, ?)",
                    "Created new ticket with value id: "+ColorTool.apply(DCColor.GREEN, id)+
                    ", identifier: "+ColorTool.apply(DCColor.GREEN, identifier.toString())+
                    ", creation_date: "+ColorTool.apply(DCColor.GOLD, creation_date)+
                    ", creatorId: "+ColorTool.apply(DCColor.GOLD, creatorId)+
                    ", handlerId: "+ColorTool.apply(DCColor.GOLD, handlerId),
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public Ticket getTicket(String id) throws SQLException, InterruptedException {
        if (!existsId(id)) {
            logger.error("{} - Failed to get ticket with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tickets WHERE id=?")) {
            statement.setString(1, id);


            try (ResultSet resultSet = statement.executeQuery()) {
                ScpTools.getLoggerManager().databaseLog(
                        "SELECT * FROM tickets WHERE id=?",
                        "Selected ticket id: "+ColorTool.apply(DCColor.GREEN, id),
                        LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                        Color.ORANGE);

                return new Ticket(
                        resultSet.getString("id"),
                        TicketIdentifier.valueOf(resultSet.getString("identifier")),
                        resultSet.getString("creation_date"),
                        resultSet.getString("creatorId"),
                        resultSet.getString("handlerId"));
            }
        }
    }

    public void updateHandler(String id, String handlerId) throws SQLException, InterruptedException {
        if (!existsId(id)) {
            logger.error("{} - Failed to update ticket with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE tickets SET handlerId = ? WHERE id = ?")) {
            statement.setString(1, handlerId);
            statement.setString(2, id);
            statement.executeUpdate();
            logger.info("{} - Updated handler of ticket - id: {} , handlerId: {}", prefix,
                    ColorTool.apply(DCColor.GREEN, id),
                    ColorTool.apply(DCColor.GOLD, handlerId));
            ScpTools.getLoggerManager().databaseLog(
                    "UPDATE tickets SET handlerId = ? WHERE id = ?",
                    "Updated ticket handlerId with id: "+ColorTool.apply(DCColor.GREEN, id)+
                    ", handlerId: "+ColorTool.apply(DCColor.GOLD, handlerId),
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public void deleteTicket(String id) throws SQLException, InterruptedException {
        if  (!existsId(id)) {
            logger.error("{} - Failed to delete ticket with id: {}. Id does not exist", prefix,
                    ColorTool.apply(DCColor.GREEN, id));
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tickets WHERE id=?")) {
            statement.setString(1, id);
            statement.execute();
            logger.info("{} - Deleted ticket - id: {}", prefix,
                    ColorTool.apply(DCColor.RED, id));
            ScpTools.getLoggerManager().databaseLog(
                    "DELETE FROM tickets WHERE id=?",
                    "Deleted ticket with id:  "+ColorTool.apply(DCColor.GREEN, id),
                    LoadedConfigurations.getConfigMemoryLoad().database_logging_channel_id(),
                    Color.ORANGE);
        }
    }

    public boolean existsId(String id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM tickets WHERE id=?);")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean existsIdentifier(TicketIdentifier identifier) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM tickets WHERE identifier=?);")) {
            statement.setString(1, identifier.toString());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
