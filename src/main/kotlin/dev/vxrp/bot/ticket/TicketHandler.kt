package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.bot.ticket.data.TicketTypes
import dev.vxrp.bot.ticket.enums.TicketStatus
import dev.vxrp.bot.ticket.enums.TicketType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.TicketTable
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.interactions.modals.ModalMapping
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDate

class TicketHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(TicketHandler::class.java)

    suspend fun createTicket(ticketType: TicketType, ticketStatus: TicketStatus, ticketCreator: String, ticketHandler: User?, modalId: String, modalValue: MutableList<ModalMapping>): ThreadChannel? {
        val settings = querySettings(ticketType)
        if (settings == null) {
            logger.error("Can't create ticket, settings are not loaded")
            return null
        }

        val channel = api.getTextChannelById(settings.parentChannel)
        if (channel == null) {
            logger.error("Could not find specified parent channel for '{}' ticket", settings.name)
            return null
        }

        val child = channel.createThreadChannel(settings.childRules.parentName.replace("%r%", TicketTable().retrieveSerial().toString()), true).await()
        if (ticketCreator != "anonymous") {
            val creatorUser = api.retrieveUserById(ticketCreator).await()
            child.sendMessage(creatorUser.asMention).await().delete().queue()
        }
        for (roleId in settings.roles) {
            val role = api.getRoleById(roleId)

            if (role == null) {
                logger.error("Could not find role {} for ticket {}, does it exist?", roleId, settings.name)
                continue
            }

            child.sendMessage(role.asMention).await().delete().queue()
        }

        val logMessage = TicketLogHandler(api, config, translation).logMessage(ticketCreator, ticketHandler, child.id, ticketStatus, child) ?: run {
            logger.error("Could not carry out log message correctly")
            return null
        }
        val message = TicketMessageHandler(api, config, translation).sendMessage(ticketType, child, ticketCreator, modalId, modalValue)
        TicketTable().addToDatabase(child.id, LocalDate.now(), ticketType, ticketStatus, ticketCreator, ticketHandler, logMessage, message.id, "CURRENTLY NOT IMPLEMENTED")
        logger.info("Created ticket: {} of type: {} by user: {} with current status {}", child.id, ticketType, ticketCreator, ticketStatus)
        return child
    }

    private fun querySettings(ticketType: TicketType): TicketTypes? {
        for (option in config.ticket.types) {
            if (option.type.replace("support::", "") != ticketType.toString()) continue
            return option
        }
        logger.error("Could not correctly load ticket settings")
        return null
    }
}