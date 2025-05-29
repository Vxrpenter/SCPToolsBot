package dev.vxrp.bot.regulars.handler

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.interactions.components.buttons.Button
import kotlin.math.roundToInt

class RegularsMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    fun sendRegulars(channel: TextChannel) {
        val embeds = mutableListOf<MessageEmbed>()
        val embed = Embed {
            thumbnail = api.getGuildById(config.settings.guildId)?.iconUrl
            title = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateBody)
        }
        embeds.add(embed)

        val regulars = RegularsFileHandler(config).query()
        for (regular in regulars) {
            val stringBuilder: StringBuilder = StringBuilder()

            for (role in regular.config.roles) {
                stringBuilder.append(ColorTool().useCustomColorCodes(translation.regulars.embedTemplateRoleBody
                    .replace("%role%", "<@&${role.id}>")
                    .replace("%description%", role.description)
                    .replace("%timeframe%", if (role.requirementType == "PLAYTIME" || role.requirementType == "BOTH") translation.regulars.textTemplateRoleTimeframe else "")
                    .replace("%playtime%", if (role.requirementType == "PLAYTIME" || role.requirementType == "BOTH") role.playtimeRequirements.toString()+"h" else "")
                    .replace("%xp%", if (role.requirementType == "XP" || role.requirementType == "BOTH") translation.regulars.textTemplateRoleXp else "")
                    .replace("%level%", if (role.requirementType == "XP" || role.requirementType == "BOTH") role.xpRequirements.toString()+" Level" else "")))
            }

            val groupEmbed = Embed {
                description = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateGroupBody
                    .replace("%group%", regular.manifest.name)
                    .replace("%description%", regular.manifest.description)
                    .replace("%group_role%", "<@&${regular.manifest.customRole.id}>").replace("<@&>", "None")
                    .replace("%roles%", stringBuilder.toString()))
            }

            embeds.add(groupEmbed)
        }

        channel.send("", embeds).addActionRow(
            Button.success("regulars_open_settings", translation.buttons.textRegularOpenSettings)
        ).queue()
    }

    fun getSettings(user: User, injectTitle: String? = null, injectDescription: String? = null): MessageEmbed {
        var groupRole = "None"
        var role = "None"
        var playtime = "0"
        var level = "0"
        var requirementType = ""
        var lastChecked = "None"

        if (RegularsTable().exists(user.id)) {
            val regularRole = RegularsFileHandler(config).queryRoleFromConfig(RegularsTable().getGroup(user.id), RegularsTable().getRole(user.id)!!)

            requirementType = regularRole?.requirementType ?: ""
            groupRole = "<@&${RegularsTable().getGroupRole(user.id) ?: "None" }>"
            role = "<@&${RegularsTable().getRole(user.id) ?: "None"}>"
            playtime = RegularsTable().getPlaytime(user.id).roundToInt().toString()
            level = RegularsTable().getLevel(user.id).toString()
            lastChecked = RegularsTable().getLastChecked(user.id) ?: "None"
        }

        var embedTitle = injectTitle
        var embedDescription = injectDescription
        if (injectTitle == null) embedTitle = translation.regulars.embedSettingsTitle
        if (injectDescription == null) embedDescription = translation.regulars.embedSettingsBody

        val embed = Embed {
            thumbnail = user.avatarUrl
            title = ColorTool().useCustomColorCodes(embedTitle)
            description = ColorTool().useCustomColorCodes(embedDescription)
            field {
                inline = true
                name = translation.regulars.embedSettingsFieldGroupName
                value = groupRole.replace("<@&>", "None")
            }
            field {
                inline = true
                name = translation.regulars.embedSettingsFieldRoleName
                value = role.replace("<@&>", "None")
            }
            if (requirementType == "PLAYTIME" || requirementType == "BOTH") {
                field {
                    inline = true
                    name = translation.regulars.embedSettingsFieldPlaytimeName
                    value = playtime
                }
            }
            if (requirementType == "XP" || requirementType == "BOTH") {
                field {
                    inline = true
                    name = translation.regulars.embedSettingsFieldXp
                    value = level
                }
            }
            field {
                inline = true
                name = translation.regulars.embedSettingsFieldLastChecked
                value = lastChecked
            }
            if (lastChecked != "None") {
                field {
                    inline = true
                    name = ""
                    value = ""
                }
            }
        }

        return embed
    }

    fun getSettingsActionRow(userId: String): List<Button> {
        var syncButton = Button.success("regulars_sync", translation.buttons.textRegularSync)
        var syncReactivateButton = Button.success("regulars_sync_reactivate", translation.buttons.textRegularSyncReactivate)
        var syncDeactivateButton = Button.danger("regulars_sync_deactivate", translation.buttons.textRegularSyncDeactivate)
        var syncRemoveButton = Button.danger("regulars_sync_remove", translation.buttons.textRegularSyncRemove)

        if (!RegularsTable().exists(userId)) {
            syncReactivateButton = syncReactivateButton.asDisabled()
            syncDeactivateButton= syncDeactivateButton.asDisabled()
            syncRemoveButton = syncRemoveButton.asDisabled()

            return listOf(syncButton, syncReactivateButton, syncDeactivateButton, syncRemoveButton)
        }

        if (RegularsTable().getActive(userId)) {
            syncButton = syncButton.asDisabled()
            syncReactivateButton = syncReactivateButton.asDisabled()
        } else {
            syncDeactivateButton = syncDeactivateButton.asDisabled()
        }

        return listOf(syncButton, syncReactivateButton, syncDeactivateButton, syncRemoveButton)
    }
}