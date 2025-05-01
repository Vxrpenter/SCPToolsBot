package dev.vxrp.bot.commands.commanexecutes.status

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.bot.status.enums.PlayerlistType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.database.StatusTable
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class TemplateCommand(val config: Config, val translation: Translation, private val statusConstructor: StatusConstructor) {

    suspend fun pastePlayerList(event: SlashCommandInteractionEvent) {
        val embed = Playerlist().getEmbed(event.jda.selfUser.id, translation, statusConstructor)

        val message = event.channel.send("", listOf(embed)).await()
        event.reply("Pasted static playerlist").setEphemeral(true).queue()
        val id = message.id


        val currentPort = statusConstructor.mappedBots[event.jda.selfUser.id]
        val server = statusConstructor.mappedServers[currentPort]

        transaction {
            StatusTable.Status.insert {
                it[type] = PlayerlistType.PRINTED.toString()
                it[channelId] = event.channel.id
                it[messageId] = id
                it[port] = server?.port.toString()
                it[created] = LocalDate.now().toString()
                it[lastUpdated] = System.currentTimeMillis().toString()
            }
        }
    }
}