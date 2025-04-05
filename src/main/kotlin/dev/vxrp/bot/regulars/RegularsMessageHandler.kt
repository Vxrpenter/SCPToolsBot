package dev.vxrp.bot.regulars

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.RegularsTable
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
            title = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateBody)
        }
        embeds.add(embed)

        val regulars = RegularsFileHandler(config, translation).query()
        for (regular in regulars) {
            val stringBuilder: StringBuilder = StringBuilder()

            for (role in regular.config.roles) {
                stringBuilder.append(ColorTool().useCustomColorCodes(translation.regulars.embedTemplateRoleBody
                    .replace("%role%", "<@&${role.id}>")
                    .replace("%description%", role.description)
                    .replace("%timeframe%", role.playtimeRequirements.toString()+"h")))
            }

            val groupEmbed = Embed {
                description = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateGroupBody
                    .replace("%group%", regular.manifest.name)
                    .replace("%description%", regular.manifest.description)
                    .replace("%group_role%", "<@&${regular.manifest.customRole.id}>")
                    .replace("%roles%", stringBuilder.toString()))
            }

            embeds.add(groupEmbed)
        }

        channel.send("", embeds).addActionRow(
            Button.success("regulars_open_settings", translation.buttons.textRegularOpenSettings)
        ).queue()
    }

    fun getSettings(user: User): MessageEmbed {
        var groupRole = "None"
        var role = "None"
        var playtime = "0"

        if (RegularsTable().exists(user.id)) {
            groupRole = "<@&${RegularsTable().getGroupRole(user.id).replace("null", "None")}>"
            role = "<@&${RegularsTable().getRole(user.id).replace("null", "None")}>"
            playtime = "<@&${RegularsTable().getPlaytime(user.id).roundToInt()}>"
        }

        val embed = Embed {
            thumbnail = user.avatarUrl
            title = ColorTool().useCustomColorCodes(translation.regulars.embedSettingsTitle)
            description = ColorTool().useCustomColorCodes(translation.regulars.embedSettingsBody
                .replace("%group%", groupRole)
                .replace("%role%", role)
                .replace("%time%", playtime))
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