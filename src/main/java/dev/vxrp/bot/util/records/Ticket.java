package dev.vxrp.bot.util.records;

import dev.vxrp.bot.util.Enums.TicketIdentifier;

public record Ticket(String id, TicketIdentifier identifier, String creation_date, String creatorId, String handlerId) {
}
