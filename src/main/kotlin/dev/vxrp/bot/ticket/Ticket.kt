package dev.vxrp.bot.ticket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val settings: List<TicketSettings>)

@Serializable
data class TicketSettings(
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
