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
    val ticketLogChannel: String,
    @SerialName("application_message_channel")
    val applicationMessageChannel: String,
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
    @SerialName("log_permissions_roles")
    val logPermissionRoles: List<String>,
    @SerialName("parent_channel")
    val parentChannel: String,
    @SerialName("child_rules")
    val childRules: TicketChildRules,
)

@Serializable
data class TicketChildRules(
    @SerialName("parent_name")
    val parentName: String,
    @SerialName("use_status_bar")
    val useStatusBar: Boolean,
    @SerialName("lock_on_default")
    val lockOnDefault: Boolean
)