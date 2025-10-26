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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val active: Boolean,

    val api: String,
    @SerialName("account_id")
    val accountId: String,

    @SerialName("post_server_status")
    val postServerStatus: Boolean,
    @SerialName("post_channel")
    val postChannel: String,
    @SerialName("page_url")
    val pageUrl: String,

    @SerialName("check_playerlist")
    val checkPlayerlist: Boolean,

    @SerialName("check_rate")
    val checkRate: Int,
    @SerialName("retry_to_fetch_data")
    val retryToFetchData: Int,
    @SerialName("suspect_rate_limit_until")
    val suspectRateLimitUntil: Int,
    @SerialName("idle_after")
    val idleAfter: Int,
    @SerialName("idle_check_rate")
    val idleCheckRate: Int,

    val instances: List<Instance>
)  { companion object { val instance by lazy { ConfigLite.load<Status>("status.yml") } } }

@Serializable
data class Instance(
    val token: String,
    val name: String,
    @SerialName("server_port")
    val serverPort: Int,
    val retries: Int,
    val playerlist: PlayerList
)

@Serializable
data class PlayerList(
    val active: Boolean,
    @SerialName("channel_ids")
    val channelId: List<String>
)