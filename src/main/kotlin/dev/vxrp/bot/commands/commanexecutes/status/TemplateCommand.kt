package dev.vxrp.bot.commands.commanexecutes.status

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.data.StatusConst
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.tables.StatusTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class TemplateCommand(val config: Config, val translation: Translation, private val statusConst: StatusConst) {

    suspend fun pastePlayerList(event: SlashCommandInteractionEvent) {
        val embed = Playerlist().getEmbed(event.jda.selfUser.id, translation, statusConst)

        val message = event.channel.send("", listOf(embed)).await()
        event.reply("Pasted static playerlist").setEphemeral(true).queue()
        val id = message.id


        val currentPort = statusConst.mappedBots[event.jda.selfUser.id]
        val server = statusConst.mappedServers[currentPort]

        transaction {
            StatusTable.Status.insert {
                it[channelId] = event.channel.id
                it[messageId] = id
                it[port] = server?.port.toString()
            }
        }
    }
}