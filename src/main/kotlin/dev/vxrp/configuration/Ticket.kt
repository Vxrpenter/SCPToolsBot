/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.configuration

import dev.vxrp.configlite.ConfigLite
import dev.vxrp.translationName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val settings: TicketSettings,
    @SerialName("application_types")
    val applicationTypes: List<ApplicationTypes>,
    val types: List<TicketTypes>
) {
    companion object {
        val instance by lazy {
            ConfigLite.load<Ticket>("tickets.yml")
        }
    }
}

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