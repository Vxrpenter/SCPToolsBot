package dev.vxrp.bot.ticket

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.TicketTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.modals.ModalMapping
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.Instant
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

        val child = channel.createThreadChannel(settings.childRules.parentName.replace("%r%", retrieveSerial().toString()), true).await()
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

        var thumbnailUrl = "https://www.pngarts.com/files/4/Anonymous-Mask-Transparent-Images.png"
        var creatorUserMention = "anonymous"
        var creatorUserName = "anonymous"
        var handlerUserName = "none"

        if (ticketCreator != "anonymous") {
            val creatorUser = api.retrieveUserById(ticketCreator).await()
            creatorUserName = creatorUser.globalName!!
            thumbnailUrl = creatorUser.avatarUrl.toString()
            creatorUserMention = api.retrieveUserById(ticketCreator).await().asMention
        }
        if (ticketHandler != null) handlerUserName = ticketHandler.asMention

        val logMessage = logMessage(thumbnailUrl, child.name, creatorUserName, creatorUserMention, handlerUserName, child.id, ticketStatus) ?: run {
            logger.error("Could not carry out log message correctly")
            return null
        }

        addToDatabase(child.id, LocalDate.now(), ticketType, ticketStatus, ticketCreator, ticketHandler, logMessage)
        sendMessage(ticketType, child, ticketCreator, modalId, modalValue)
        return child
    }

    fun claimTicket(id: String, userId: String) {
        updateTicketHandler(id, userId)
    }

    fun openTicket(id: String) {
        updateTicketStatus(id, TicketStatus.OPEN)
    }

    fun pauseTicket(id: String) {
        api.getThreadChannelById(id)!!.manager.setLocked(true).queue()
        updateTicketStatus(id, TicketStatus.PAUSED)
    }

    fun suspendTicket(id: String) {
        api.getThreadChannelById(id)!!.manager.setLocked(true).queue()
        updateTicketStatus(id, TicketStatus.SUSPENDED)
    }

    fun archiveTicket(id: String) {
        api.getThreadChannelById(id)!!.manager.setArchived(true).queue()
        updateTicketStatus(id, TicketStatus.CLOSED)
    }

    private suspend fun logMessage(thumbnailUrl: String, channelName: String, creator: String, creatorMention: String, handler: String, ticketId: String, ticketStatus: TicketStatus): String? {
        val logEmbed = Embed {
            thumbnail = thumbnailUrl
            title = ColorTool().useCustomColorCodes(translation.support.embedLogTitle
                .replace("%name%", channelName)
                .replace("%user%", creator))
            description = ColorTool().useCustomColorCodes(translation.support.embedLogBody
                .replace("%status%", ticketStatus.toString())
                .replace("%id%", ticketId)
                .replace("%creator%", creatorMention)
                .replace("%handler%", handler))
            timestamp = Instant.now()
        }
        val channel =  api.getTextChannelById(config.ticket.settings.ticketLogChannel) ?: run {
            logger.error("Could not sent ticket log message for ticket '{}'", ticketId)
            return null
        }

        var isHandled = false
        if (handler != "none") isHandled = true

        return channel.send("", listOf(logEmbed)).addActionRow(
            logActionRow(ticketStatus, isHandled, ticketId)
        ).await().id
    }

    // Manager Functions
    private fun addToDatabase(ticketId: String, date: LocalDate, ticketType: TicketType, ticketStatus: TicketStatus, ticketCreator: String, ticketHandler: User?, ticketLogMessage: String) {
        transaction {
            TicketTable.Tickets.insert {
                it[id] = ticketId
                it[type] = ticketType.toString()
                it[status] = ticketStatus.toString()
                it[creation_date] = date.toString()
                it[creator] = ticketCreator
                it[handler] = ticketHandler?.id
                it[logMessage] = ticketLogMessage
            }
        }
    }

    private fun updateTicketStatus(ticketId: String, ticketStatus: TicketStatus) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[status] = ticketStatus.toString()
            }
        }
    }

    fun getTicketStatus(ticketId: String): TicketStatus? {
        var status: TicketStatus? = null
        transaction {
            TicketTable.Tickets.selectAll()
                .where(TicketTable.Tickets.id.eq(ticketId))
                .forEach {
                    status = TicketStatus.valueOf(it[TicketTable.Tickets.status])
                }
        }

        return status
    }

    private fun updateTicketHandler(ticketId: String, userId: String) {
        transaction {
            TicketTable.Tickets.update({ TicketTable.Tickets.id.eq(ticketId) }) {
                it[handler] = userId
            }
        }
    }

    private fun retrieveSerial(): Long {
        var count: Long = 0
        transaction {
            count = TicketTable.Tickets.selectAll().count()
        }

        return count
    }

    private fun querySettings(ticketType: TicketType): TicketTypes? {
        for (option in config.ticket.types) {
            if (option.type.replace("support::", "") != ticketType.toString()) continue
            return option
        }

        logger.error("Could not correctly load ticket settings")
        return null
    }

    private suspend fun sendMessage(type: TicketType, channel: ThreadChannel, userId: String, modalId: String, modalValues: MutableList<ModalMapping>) {
        when(type) {
            TicketType.GENERAL -> {
                val embed = Embed {
                    author {
                        val user = api.retrieveUserById(userId).await()
                        iconUrl = user.avatarUrl
                        name = user.name
                    }
                    title = ColorTool().useCustomColorCodes(translation.support.embedTicketGeneralTitle.replace("%name%", channel.name))
                    description = ColorTool().useCustomColorCodes(translation.support.embedTicketGeneralBody
                        .replace("%issuer%", "<@$userId>")
                        .replace("%subject%", modalValues[0].asString)
                        .replace("%explanation%", modalValues[1].asString))
                    timestamp = Instant.now()
                }

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.REPORT -> {
                val embed = Embed {
                    author {
                        val user = api.retrieveUserById(userId).await()
                        iconUrl = user.avatarUrl
                        name = user.name
                    }
                    title = ColorTool().useCustomColorCodes(translation.support.embedTicketReportTitle.replace("%name%", channel.name))
                    description = ColorTool().useCustomColorCodes(translation.support.embedTicketReportBody
                        .replace("%issuer%", "<@$userId>")
                        .replace("%reported%", "<@${modalId.split(":")[1]}>")
                        .replace("%reason%", modalValues[0].asString)
                        .replace("%proof%", modalValues[1].asString))
                    timestamp = Instant.now()
                }

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.ERROR -> {
                val embed = Embed {
                    author {
                        val user = api.retrieveUserById(userId).await()
                        iconUrl = user.avatarUrl
                        name = user.name
                    }
                    title = ColorTool().useCustomColorCodes(translation.support.embedTicketErrorTitle.replace("%name%", channel.name))
                    description = ColorTool().useCustomColorCodes(translation.support.embedTicketErrorBody
                        .replace("%issuer%", "<@$userId>")
                        .replace("%problem%", modalValues[0].asString)
                        .replace("%times%", modalValues[1].asString)
                        .replace("%reproduce%", modalValues[2].asString)
                        .replace("%additional%", modalValues[3].asString))
                    timestamp = Instant.now()
                }

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.UNBAN -> {
                val embed = Embed {
                    author {
                        val user = api.retrieveUserById(userId).await()
                        iconUrl = user.avatarUrl
                        name = user.name
                    }
                    title = ColorTool().useCustomColorCodes(translation.support.embedTicketUnbanTitle.replace("%name%", channel.name))
                    description = ColorTool().useCustomColorCodes(translation.support.embedTicketUnbanBody
                        .replace("%issuer%", "<@$userId>")
                        .replace("%steamId%", modalValues[0].asString)
                        .replace("%reason%", modalValues[1].asString))
                    timestamp = Instant.now()
                }

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }

            TicketType.COMPLAINT -> {
                var staff = "Anonymous"
                if (modalId.split(":")[1] != "anonymous") {
                    staff = "<@${modalId.split(":")[1]}>"
                }

                val embed = Embed {
                    author {
                        if (userId != "anonymous") {
                            val user = api.retrieveUserById(userId).await()
                            iconUrl = user.avatarUrl
                            name = user.globalName
                        } else {
                            iconUrl = "https://www.pngarts.com/files/4/Anonymous-Mask-Transparent-Images.png"
                            name = "Anonymous"
                        }
                    }
                    title = ColorTool().useCustomColorCodes(translation.support.embedTicketComplaintTitle.replace("%name%", channel.name))
                    description = ColorTool().useCustomColorCodes(translation.support.embedTicketComplaintBody
                        .replace("%issuer%", "<@$userId>")
                        .replace("%staff%", staff)
                        .replace("%reason%", modalValues[0].asString)
                        .replace("%proof%", modalValues[1].asString))
                    timestamp = Instant.now()
                }

                channel.send("", listOf(embed)).setActionRow(
                    Button.primary("ticket_claim", translation.buttons.ticketSupportClaim).withEmoji(Emoji.fromFormatted("🚪")),
                    Button.danger("ticket_close:$type", translation.buttons.ticketSupportClose).withEmoji(Emoji.fromFormatted("🪫")),
                    Button.secondary("ticket_settings", translation.buttons.textSupportSettings).withEmoji(Emoji.fromFormatted("⚙️"))
                ).queue()
            }
        }
    }

    fun settingsActionRow(status: TicketStatus): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        var open = Button.success("ticket_setting_open", translation.buttons.textSupportSettingsOpen).withEmoji(Emoji.fromFormatted("🚪"))
        var pause = Button.primary("ticket_setting_pause", translation.buttons.textSupportSettingsPause).withEmoji(Emoji.fromFormatted("🌙"))
        var suspend = Button.primary("ticket_setting_suspend", translation.buttons.textSupportSettingsSuspend).withEmoji(Emoji.fromFormatted("🔒"))
        var close = Button.danger("ticket_setting_close", translation.buttons.textSupportSettingsClose).withEmoji(Emoji.fromFormatted("🪫"))

        when(status) {
            TicketStatus.OPEN ->open = open.asDisabled()
            TicketStatus.PAUSED -> pause = pause.asDisabled()
            TicketStatus.SUSPENDED -> suspend = suspend.asDisabled()
            TicketStatus.CLOSED -> close = close.asDisabled()
        }

        rows.add(open)
        rows.add(pause)
        rows.add(suspend)
        rows.add(close)

        return rows
    }

    private fun logActionRow(status: TicketStatus, handler: Boolean, ticketId: String): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        var claim = Button.primary("ticket_log_claim:$ticketId", translation.buttons.textSupportLogClaim).withEmoji(Emoji.fromFormatted("📫"))
        var open = Button.success("ticket_log_open:$ticketId", translation.buttons.textSupportLogOpen).withEmoji(Emoji.fromFormatted("🚪"))
        var pause = Button.primary("ticket_log_pause:$ticketId", translation.buttons.textSupportLogPause).withEmoji(Emoji.fromFormatted("🌙"))
        var suspend = Button.primary("ticket_log_suspend:$ticketId", translation.buttons.textSupportLogSuspend).withEmoji(Emoji.fromFormatted("🔒"))
        var close = Button.danger("ticket_log_close:$ticketId", translation.buttons.textSupportLogClose).withEmoji(Emoji.fromFormatted("🪫"))

        if (handler) claim = claim.asDisabled()
        when(status) {
            TicketStatus.OPEN ->open = open.asDisabled()
            TicketStatus.PAUSED -> pause = pause.asDisabled()
            TicketStatus.SUSPENDED -> suspend = suspend.asDisabled()
            TicketStatus.CLOSED -> close = close.asDisabled()
        }

        rows.add(claim)
        rows.add(open)
        rows.add(pause)
        rows.add(suspend)
        rows.add(close)

        return rows
    }
}