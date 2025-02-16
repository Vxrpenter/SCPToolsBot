package dev.vxrp.bot.application

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.application.data.ApplicationType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.ApplicationTypeTable
import dev.vxrp.database.tables.MessageTable
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.enums.MessageType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.slf4j.LoggerFactory

val applicationTypeMap: HashMap<String, MutableList<ApplicationType>> = hashMapOf()

class ApplicationManager(val config: Config, val translation: Translation) {
    val logger = LoggerFactory.getLogger(ApplicationManager::class.java)

    fun sendActivationMenu(userId: String, channel: TextChannel): Pair<MessageEmbed, Collection<ItemComponent>> {
        val embed = createMessage(userId, true)

        return Pair(embed, applicationActionRow(userId, null))
    }

    fun sendDeactivationMenu() {
        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.application.embedDeactivationMenuTitle)
            description = ColorTool().useCustomColorCodes(translation.application.embedDeactivationMenuBody)
        }
    }

    fun editActivationMessage(userId: String, roleId: String, channel: TextChannel, messageId: String, name: String? = null, description: String? = null, emoji: String? = null, state: Boolean? = null, initializer: String? = null, member: Int? = null) {
        changeApplicationType(userId, roleId, name, description, emoji, state, initializer, member)

        channel.editMessage(messageId, "", listOf(createMessage(userId, false))).setActionRow(
            applicationActionRow(userId, applicationTypeMap[userId])
        ).queue()
    }

    suspend fun sendApplicationMessage(api: JDA, userId: String, channel: TextChannel) {
        val roleStringPair = createRoleString(userId, false)

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.application.embedApplicationMessageTitle)
            description = ColorTool().useCustomColorCodes(translation.application.embedApplicationMessageBody
                .replace("%status%", translation.application.textStatusActive)
                .replace("%active_roles%", roleStringPair.first.toString()))
        }

        val applicationMessage = MessageTable().queryFromTable(MessageType.APPLICATION)

        if (applicationMessage != null) {
            try {
                api.getTextChannelById(applicationMessage.channelId)?.editMessage(applicationMessage.id, "", listOf(embed))?.await()
            } catch (e: ErrorResponseException) {
                val message = channel.send("", listOf(embed)).addActionRow(
                    Button.success("application_open", translation.buttons.textApplicationOpenTickets).withEmoji(Emoji.fromFormatted("📩"))
                ).await()
                MessageTable().delete(applicationMessage.id)
                MessageTable().insertIfNotExists(message.id, MessageType.APPLICATION, message.channelId)
            }
            return
        }
        val message = channel.send("", listOf(embed)).addActionRow(
            Button.success("application_open", translation.buttons.textApplicationOpenTickets).withEmoji(Emoji.fromFormatted("📩"))
        ).await()

        for (type in applicationTypeMap[userId]!!) {
            ApplicationTypeTable().changeType(type.roleId, type.state, type.initializer)
        }
        MessageTable().insertIfNotExists(message.id, MessageType.APPLICATION, message.channelId)
    }

    private fun changeApplicationType(userID: String, roleID: String, name: String? = null, description: String? = null, emoji: String? = null, state: Boolean? = null, initializer: String? = null, member: Int? = null) {
        for (type in applicationTypeMap[userID]!!) {
            if (type.roleId != roleID) continue

            var typeName = type.name
            var typeDescription = type.description
            var typeEmoji = type.emoji
            var typeState = type.state
            var typeInitializer = type.initializer
            var typeMember = type.member

            if (name != null) typeName = name
            if (description != null) typeDescription = description
            if (emoji != null) typeEmoji = emoji
            if (state != null) typeState = state
            if (initializer != null) typeInitializer = initializer
            if (member != null) typeMember = member

            val applicationTypeList = applicationTypeMap[userID]!!

            applicationTypeList[type.pos] = ApplicationType(type.pos, type.roleId, typeName, typeDescription, typeEmoji, typeState, typeInitializer, typeMember)

            break
        }
    }

    private fun createMessage(userId: String, useBaseValue: Boolean): MessageEmbed {
        val roleStringPair = createRoleString(userId, useBaseValue)

        applicationTypeMap[userId] = roleStringPair.second

        return Embed {
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