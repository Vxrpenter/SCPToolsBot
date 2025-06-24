/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.application.handler

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.application.ApplicationManager
import dev.vxrp.util.applicationTypeSet
import dev.vxrp.bot.application.data.ApplicationType
import dev.vxrp.bot.application.enums.MessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
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
        val embed = createMessage(true)

        return Pair(embed, applicationActionRow(userId, null))
    }

    fun getDeactivationMenu(): Pair<MessageEmbed, Collection<ItemComponent>> {
        val embed = Embed {
            color = 0xE74D3C
            title = ColorTool().parse(translation.application.embedDeactivationMenuTitle)
            description = ColorTool().parse(translation.application.embedDeactivationMenuBody)
        }

        val actionRow: MutableCollection<ItemComponent> = ArrayList()
        actionRow.add(Button.danger("application_deactivate", translation.buttons.textApplicationDeactivate).withEmoji(Emoji.fromFormatted("🪫")))

        return Pair(embed, actionRow)
    }

    fun editActivationMessage(userId: String, roleId: String, channel: TextChannel, messageId: String, name: String? = null, description: String? = null, emoji: String? = null, state: Boolean? = null, initializer: String? = null, member: Int? = null) {
        ApplicationManager(config, translation).changeApplicationType(roleId, name, description, emoji, state, initializer, member)

        channel.editMessage(messageId, "", listOf(createMessage(false))).setActionRow(applicationActionRow(userId, applicationTypeSet.toList())).queue()
    }

    suspend fun sendApplicationMessage(api: JDA, channel: TextChannel, state: Boolean) {
        val roleStringPair = createRoleString(false)

        var status = translation.application.textStatusActive
        if (!state) status = translation.application.textStatusDeactivated

        var embedColor = 0x2ECC70
        if (!state) embedColor = 0xE74D3C

        val embed = Embed {
            color = embedColor
            title = ColorTool().parse(translation.application.embedApplicationMessageTitle)
            description = ColorTool().parse(translation.application.embedApplicationMessageBody
                .replace("%status%", status)
                .replace("%active_roles%", roleStringPair.toString())
            )
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

        for (type in applicationTypeSet) ApplicationTypeTable().changeType(type.roleId, type.state, type.member,type.initializer)
        if (message == null) {
            logger.error("Could not mirror applied application settings to database")
            return
        }

        MessageTable().insertIfNotExists(message.id, MessageType.APPLICATION, message.channelId)
    }

    private fun createMessage(useBaseValue: Boolean): MessageEmbed {
        val roleStringPair = createRoleString(useBaseValue)

        return Embed {
            color = 0x2ECC70
            title = ColorTool().parse(translation.application.embedActivationMenuTitle)
            description = ColorTool().parse(translation.application.embedActivationMenuBody
                .replace("%status%", translation.application.textStatusDeactivated)
                .replace("%active_roles%", roleStringPair.toString())
            )
        }
    }

    private fun createRoleString(useBaseValue: Boolean): StringBuilder {
        val applicationTypeList: MutableList<ApplicationType> = mutableListOf()

        if (useBaseValue && ApplicationTypeTable().getAllEntrys() != null) {
            val baseTypes = createStringBaseValue(applicationTypeList)
            applicationTypeSet = baseTypes.toHashSet()

            for (type in ApplicationTypeTable().getAllEntrys()!!) {
                baseTypes.filter { it.roleId == type.roleId }.forEach { ApplicationManager(config, translation).changeApplicationType(type.roleId, it.name, it.description, it.emoji, type.active, type.initializer, type.members) }
            }
        }
        return createStringValue()
    }

    private fun createStringBaseValue(applicationTypeList: MutableList<ApplicationType>): MutableList<ApplicationType> {
        val stringBuilder: StringBuilder = StringBuilder()
        val deactivated = ColorTool().parse(translation.application.textStatusDeactivated)
        var count = 0
        for (type in config.ticket.applicationTypes) {
            count += 1

            applicationTypeList.add(ApplicationType(count, type.roleID, type.name, type.description, type.emoji, false, null, 0))

            stringBuilder.append(ColorTool().parse(translation.application.textRoleStatusTemplate
                .replace("%roleId%", type.roleID)
                .replace("%status%", deactivated)
                .replace("%max_candidates%", "0")))
        }

        return applicationTypeList
    }

    private fun createStringValue(): StringBuilder {
        val stringBuilder: StringBuilder = StringBuilder()
        val activated = ColorTool().parse(translation.application.textStatusActive)
        val deactivated = ColorTool().parse(translation.application.textStatusDeactivated)

        for (type in applicationTypeSet) {
            var status: String? = null
            if (type.state) status = activated
            if (!type.state) status = deactivated

            stringBuilder.append(ColorTool().parse(translation.application.textRoleStatusTemplate
                .replace("%roleId%", type.roleId)
                .replace("%status%", status!!)
                .replace("%max_candidates%", type.member.toString())))
        }

        return stringBuilder
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