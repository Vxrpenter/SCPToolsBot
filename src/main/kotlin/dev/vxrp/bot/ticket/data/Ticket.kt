package dev.vxrp.bot.ticket.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val settings: TicketSettings,
    @SerialName("application_types")
    val applicationTypes: List<ApplicationTypes>,
    val types: List<TicketTypes>)

@Serializable
data class TicketSettings(
    @SerialName("ticket_log_channel")
    val ticketLogChannel: String
)

@Serializable
data class ApplicationTypes(
    val name: String,
    val description: String,
    val emoji: String,
    @SerialName("role_id")
    val roleID: String
)


@Serializable
data class TicketTypes(
    val name: String,
    val type: String,
    val roles: List<String>,
    @SerialName("parent_channel")
    val parentChannel: String,
    @SerialName("child_rules")
    val childRules: TicketChildRules,
)

@Serializable
data class TicketChildRules(
    @SerialName("parent_name")
    val parentName: String,
    @SerialName("allow_settings")
    val allowSettings: Boolean,
    @SerialName("use_status_bar")
    val useStatusBar: Boolean,
    val logging: TicketLogging,
    @SerialName("lock_on_default")
    val lockOnDefault: Boolean
)

@Serializable
data class TicketLogging(
    val messages: Boolean,
    val files: Boolean
)
