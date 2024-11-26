package dev.vxrp.bot.status.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val active: Boolean,
    val cooldown: Int,
    val api: String,
    @SerialName("account_id")
    val accountId: String,
    val instances: List<Instance> )

@Serializable
data class Instance(
    val token: String,
    @SerialName("server_port")
    val serverPort: Int)