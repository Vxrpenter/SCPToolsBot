package dev.vxrp.bot.commands.commanexecutes.database

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.enums.DCColor

class TableStyles {
    data class PlayerList(val channelId: String, val messageId: String, val port: String)
    data class NoticeOfDeparture(val id: String, val handlerId: String, val channelId: String, val messageId: String, val beginDate: String, val endDate: String)
    data class Regulars(val id: String, val active: Boolean, val steamId: String, val groupId: String, val roleId: String, val lastCheckedDate: String)
    data class Tickets(val id: String, val identifier: String, val status: String, val creationDate: String, val creatorId: String, val handlerId: String)

    fun noticeOfDepartureTable(playerlist: List<NoticeOfDeparture>): String {
        return table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
                alignment = TextAlignment.TopCenter
            }

            header {
                cellStyle {
                    border = true
                    alignment = TextAlignment.BottomLeft
                }
                row {
                    cell(ColorTool().apply(DCColor.BOLD, "Notice Of Departures")) {
                        alignment = TextAlignment.BottomCenter
                        columnSpan = 6
                    }
                }
            }
            body {
                row("id", "handler_id", "channel_id", "message_id", "begin_date", "end_date")
                for (list in playerlist) {
                    row(list.id, list.handlerId, list.channelId, list.messageId, list.beginDate, list.endDate)
                }
            }
        }.toString()
    }

    fun playerlistTable(playerlist: List<PlayerList>): String {
        return table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
                alignment = TextAlignment.TopCenter
            }

            header {
                cellStyle {
                    border = true
                    alignment = TextAlignment.BottomLeft
                }
                row {
                    cell(ColorTool().apply(DCColor.BOLD, "Playerlists")) {
                        alignment = TextAlignment.BottomCenter
                        columnSpan = 3
                    }
                }
            }
            body {
                row("channel_id", "message_id", "port")
                for (list in playerlist) {
                    row(list.channelId, list.messageId, list.port)
                }
            }
        }.toString()
    }

    fun regularsTable(regulars: List<Regulars>): String {
        return table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
                alignment = TextAlignment.TopCenter
            }

            header {
                cellStyle {
                    border = true
                    alignment = TextAlignment.BottomLeft
                }
                row {
                    cell(ColorTool().apply(DCColor.BOLD, "Regulars")) {
                        alignment = TextAlignment.BottomCenter
                        columnSpan = 6
                    }
                }
            }
            body {
                row("id", "active", "steam_id", "group_id", "role_id", "last_checked_date")
                for (list in regulars) {
                    row(list.id, list.active, list.steamId, list.groupId, list.roleId, list.lastCheckedDate)
                }
            }
        }.toString()
    }

    fun ticketsTable(tickets: List<Tickets>): String {
        return table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
                alignment = TextAlignment.TopCenter
            }

            header {
                cellStyle {
                    border = true
                    alignment = TextAlignment.BottomLeft
                }
                row {
                    cell(ColorTool().apply(DCColor.BOLD, "Regulars")) {
                        alignment = TextAlignment.BottomCenter
                        columnSpan = 6
                    }
                }
            }
            body {
                row("id", "identifier", "status", "creationDate", "creatorId", "handlerId")
                for (list in tickets) {
                    row(list.id, list.identifier, list.status, list.creationDate, list.creatorId, list.handlerId)
                }
            }
        }.toString()
    }
}