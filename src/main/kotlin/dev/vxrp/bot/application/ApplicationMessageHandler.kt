package dev.vxrp.bot.application

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.application.data.ApplicationType
import dev.vxrp.bot.application.enums.MessageType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.database.ApplicationTypeTable
import dev.vxrp.database.tables.database.MessageTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ApplicationMessageHandler(val config: Config, val translation: Translation) {
    private val logger: Logger = LoggerFactory.getLogger(ApplicationManager::class.java)

    fun getActivationMenu(userId: String): Pair<MessageEmbed, Collection<ItemComponent>> {
        val embed = createMessage(userId, true)

        return Pair(embed, applicationActionRow(userId, null))
    }

    fun getDeactivationMenu(): Pair<MessageEmbed, Collection<ItemComponent>> {
        val embed = Embed {
            color = 0xE74D3C
            title = ColorTool().useCustomColorCodes(translation.application.embedDeactivationMenuTitle)
            description = ColorTool().useCustomColorCodes(translation.application.embedDeactivationMenuBody)
        }

        val actionRow: MutableCollection<ItemComponent> = ArrayList()
        actionRow.add(Button.danger("application_deactivate", translation.buttons.textApplicationDeactivate).withEmoji(Emoji.fromFormatted("🪫")))

        return Pair(embed, actionRow)
    }

    fun editActivationMessage(userId: String, roleId: String, channel: TextChannel, messageId: String, name: String? = null, description: String? = null, emoji: String? = null, state: Boolean? = null, initializer: String? = null, member: Int? = null) {
        ApplicationManager(config, translation).changeApplicationType(userId, roleId, name, description, emoji, state, initializer, member)

        channel.editMessage(messageId, "", listOf(createMessage(userId, false))).setActionRow(
            applicationActionRow(userId, applicationTypeMap[userId])
        ).queue()
    }

    suspend fun sendApplicationMessage(api: JDA, userId: String, channel: TextChannel, state: Boolean) {
        val roleStringPair = createRoleString(userId, false)

        var status = translation.application.textStatusActive
        if (!state) status = translation.application.textStatusDeactivated

        var embedColor = 0x2ECC70
        if (!state) embedColor = 0xE74D3C

        val embed = Embed {
            color = embedColor
            title = ColorTool().useCustomColorCodes(translation.application.embedApplicationMessageTitle)
            description = ColorTool().useCustomColorCodes(translation.application.embedApplicationMessageBody
                .replace("%status%", status)
                .replace("%active_roles%", roleStringPair.first.toString()))
        }

        val applicationMessage = MessageTable().queryFromTable(MessageType.APPLICATION)

        var message: Message?

        if (applicationMessage != null) {
            try {
                message = api.getTextChannelById(applicationMessage.channelId)?.editMessage(applicationMessage.id, "", listOf(embed))?.await()
            } catch (_: ErrorResponseException) {
                message = channel.send("", listOf(embed)).addActionRow(
                    Button.success("application_open", translation.buttons.textApplicationOpenTickets).withEmoji(Emoji.fromFormatted("📩"))
                ).await()
                MessageTable().delete(applicationMessage.id)
                MessageTable().insertIfNotExists(message.id, MessageType.APPLICATION, message.channelId)
            }
        } else {
            message = channel.send("", listOf(embed)).addActionRow(
                Button.success("application_open", translation.buttons.textApplicationOpenTickets).withEmoji(Emoji.fromFormatted("📩"))
            ).await()
        }

        for (type in applicationTypeMap[userId]!!) {
            ApplicationTypeTable().changeType(type.roleId, type.state, type.member,type.initializer)
        }
        if (message == null) {
            logger.error("Could not mirror applied application settings to database")
            return
        }

        MessageTable().insertIfNotExists(message.id, MessageType.APPLICATION, message.channelId)
    }

    private fun createMessage(userId: String, useBaseValue: Boolean): MessageEmbed {
        val roleStringPair = createRoleString(userId, useBaseValue)

        applicationTypeMap[userId] = roleStringPair.second

        return Embed {
            color = 0x2ECC70
            title = ColorTool().useCustomColorCodes(translation.application.embedActivationMenuTitle)
            description = ColorTool().useCustomColorCodes(translation.application.embedActivationMenuBody
                .replace("%status%", translation.application.textStatusDeactivated)
                .replace("%active_roles%", roleStringPair.first.toString()))
        }
    }

    private fun createRoleString(userId: String, useBaseValue: Boolean): Pair<StringBuilder, MutableList<ApplicationType>> {
        val applicationTypeList: MutableList<ApplicationType> = mutableListOf()

        var pairValue: Pair<StringBuilder, MutableList<ApplicationType>> = createStringBaseValue(applicationTypeList)
        if (!useBaseValue) pairValue = createStringValue(applicationTypeMap[userId]!!)

        return pairValue
    }

    private fun createStringBaseValue(applicationTypeList: MutableList<ApplicationType>): Pair<StringBuilder, MutableList<ApplicationType>> {
        val stringBuilder: StringBuilder = StringBuilder()
        val deactivated = ColorTool().useCustomColorCodes(translation.application.textStatusDeactivated)
        var count: Int = -1
        for (type in config.ticket.applicationTypes) {
            count += 1

            applicationTypeList.add(ApplicationType(count, type.roleID, type.name,type.description, type.emoji, false, null, 0))

            stringBuilder.append(
                ColorTool().useCustomColorCodes(translation.application.textRoleStatusTemplate
                    .replace("%roleId%", type.roleID)
                    .replace("%status%", deactivated)
                    .replace("%max_candidates%", "0")))
        }

        return Pair(stringBuilder, applicationTypeList)
    }

    private fun createStringValue(applicationTypeList: MutableList<ApplicationType>): Pair<StringBuilder, MutableList<ApplicationType>> {
        val stringBuilder: StringBuilder = StringBuilder()
        val activated = ColorTool().useCustomColorCodes(translation.application.textStatusActive)
        val deactivated = ColorTool().useCustomColorCodes(translation.application.textStatusDeactivated)

        for (type in applicationTypeList) {
            var status: String? = null
            if (type.state) status = activated
            if (!type.state) status = deactivated

            stringBuilder.append(
                ColorTool().useCustomColorCodes(translation.application.textRoleStatusTemplate
                    .replace("%roleId%", type.roleId)
                    .replace("%status%", status!!)
                    .replace("%max_candidates%", type.member.toString())))
        }

        return Pair(stringBuilder, applicationTypeList)
    }

    private fun applicationActionRow(userId: String, types: List<ApplicationType>?): Collection<ItemComponent> {
        val rows: MutableCollection<ItemComponent> = ArrayList()

        val add = Button.success("application_activation_add:$userId", translation.buttons.textApplicationActivationAdd).withEmoji(Emoji.fromFormatted("➕"))
        var remove = Button.danger("application_activation_remove:$userId", translation.buttons.textApplicationActivationRemove).withEmoji(Emoji.fromFormatted("➖"))
        var completeSetup = Button.primary("application_activation_complete_setup:$userId", translation.buttons.textApplicationActivationCompleteSetup).withEmoji(Emoji.fromFormatted("💽"))

        if (types == null) {
            remove = remove.asDisabled()
            completeSetup = completeSetup.asDisabled()
        }

        rows.add(add)
        rows.add(remove)
        rows.add(completeSetup)

        return rows
    }
}