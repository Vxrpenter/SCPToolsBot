package dev.vxrp.api.discord.data

import kotlinx.serialization.Serializable

@Serializable
data class DiscordUser(
    val id: String,
    val username: String,
)