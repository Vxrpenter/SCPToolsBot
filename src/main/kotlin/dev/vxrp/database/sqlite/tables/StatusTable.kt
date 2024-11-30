package dev.vxrp.database.sqlite.tables

import org.jetbrains.exposed.sql.Table

class StatusTable {
    object Status : Table("playerlist") {
        val channelId = text("channel_id")
        val messageId = text("message_id")
        val port = text("port")
    }
}