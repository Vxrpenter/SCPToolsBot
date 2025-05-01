package dev.vxrp.database.data

import dev.vxrp.bot.status.enums.PlayerlistType

data class StatusDatabaseEntry(
    val type: PlayerlistType,
    val channelId: String,
    val messageId: String,
    val port: String,
    val created: String,
    val lastUpdated: String
)