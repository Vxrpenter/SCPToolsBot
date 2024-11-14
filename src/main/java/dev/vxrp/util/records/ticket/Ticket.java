package dev.vxrp.util.records.ticket;

import dev.vxrp.util.Enums.TicketIdentifier;

public record Ticket(String id, TicketIdentifier identifier, String creation_date, String creatorId, String handlerId) {
}
