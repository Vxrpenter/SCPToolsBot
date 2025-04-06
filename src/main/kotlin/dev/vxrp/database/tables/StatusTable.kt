package dev.vxrp.database.tables

import org.jetbrains.exposed.sql.Table

class StatusTable {
    object Status : Table("playerlist") {
        val type = text("type")
        val channelId = text("channel_id")
        val messageId = text("message_id")
        val port = text("port")
        val created = text("created")
        val lastUpdated = text("last_updated")
    }
}