package dev.vxrp.bot.commands.commanexecutes.database

import com.jakewharton.picnic.CellStyle
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.database.sqlite.tables.StatusTable
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.jetbrains.exposed.sql.transactions.transaction

class TableSubCommand {
    data class PlayerList(val channelId: String, val messaged: String, val port: String)

    fun pasteTable(event: SlashCommandInteractionEvent) {
        event.reply_("```${chooseTable(event)}```").setEphemeral(true).queue()
    }

    private fun chooseTable(event: SlashCommandInteractionEvent): String {
        if (event.getOption("table")?.asString.equals("notice_of_departures")) {

        }

        if (event.getOption("table")?.asString.equals("playerlist")) {
            val playerListList = mutableListOf<PlayerList>()

            transaction {
                StatusTable.Status.select(StatusTable.Status.channelId, StatusTable.Status.messageId, StatusTable.Status.port)
                    .forEach {
                        val channelId = it[StatusTable.Status.channelId]
                        val messageId = it[StatusTable.Status.messageId]
                        val port = it[StatusTable.Status.port]

                        playerListList += PlayerList(channelId, messageId, port)
                    }
            }

            return playerlistTable(playerListList)
        }

        if (event.getOption("table")?.asString.equals("regulars")) {

        }

        if (event.getOption("table")?.asString.equals("tickets")) {

        }

        return ""
    }

    fun playerlistTable(playerlist: List<PlayerList>): String {
        return table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
                alignment = TextAlignment.TopCenter
            }

            row("channel_id", "message_id", "port")
            for (list in playerlist) {
                row(list.channelId, list.messaged, list.port)
            }
        }.toString()
    }
}