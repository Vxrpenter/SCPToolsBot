package dev.vxrp.api.discord.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordConnection(
    val id: String,
    val name: String,
    val type: String,
    @SerialName("friend_sync")
    val friendSync: Boolean,
    @SerialName("metadata_visibility")
    val metadataVisibility: Int,
    @SerialName("show_activity")
    val showActivity: Boolean,
    @SerialName("two_way_link")
    val twoWayLink: Boolean,
    val verified: Boolean,
    val visibility: Int
)