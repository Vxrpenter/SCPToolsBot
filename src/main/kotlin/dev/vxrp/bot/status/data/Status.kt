package dev.vxrp.bot.status.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val active: Boolean,
    @SerialName("check_playerlist")
    val checkPlayerlist: Boolean,
    @SerialName("initialize_commands")
    val initializeCommands: Boolean,
    @SerialName("initialize_event_listener")
    val initializeListeners: Boolean,
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
    val api: String,
    @SerialName("account_id")
    val accountId: String,
    val instances: List<Instance>
)

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