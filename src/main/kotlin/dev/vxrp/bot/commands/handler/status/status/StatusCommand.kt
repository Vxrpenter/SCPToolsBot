package dev.vxrp.bot.commands.handler.status.status

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.bot.commands.data.StatusConstructor
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.ConnectionTable
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatusCommand(val config: Config, val translation: Translation, private val statusConstructor: StatusConstructor) {
    fun changeMaintenanceState(event: SlashCommandInteractionEvent) {
        val currentPort = statusConstructor.mappedBots[event.jda.selfUser.id]
        val currentMaintenance = ConnectionTable().queryFromTable(currentPort.toString()).maintenance == true

        if (currentMaintenance) {
            var reason: String = translation.status.embedMaintenanceOffReasonFieldValue
            if (event.getOption("reason")?.asString != null) {
                reason = event.getOption("reason")?.asString!!
            }

            ConnectionTable().setMaintenance(currentPort.toString(), false)
            val deactivatedEmbed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.status.embedStatusDeactivatedTitle)
                description = ColorTool().useCustomColorCodes(translation.status.embedStatusDeactivatedBody)
            }

            event.reply_("", listOf(deactivatedEmbed))
                .setEphemeral(true).queue()

            val embed = Embed {
                color = 0x2ECC70
                url = config.status.pageUrl
                title = ColorTool().useCustomColorCodes(
                    translation.status.embedMaintenanceOffTitle
                        .replace("%instance%", statusConstructor.instance.name)
                ).trimIndent()
                description = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffBody).trimIndent()
                field {
                    name = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOffReasonFieldName)
                        .trimIndent()
                    value =
                        ColorTool().useCustomColorCodes(reason).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        } else {
            var reason: String = translation.status.embedMaintenanceOnReasonFieldValue
            if (event.getOption("reason")?.asString != null) {
                reason = event.getOption("reason")?.asString!!
            }

            ConnectionTable().setMaintenance(currentPort.toString(), true)
            val activatedEmbed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.status.embedStatusActivatedTitle)
                description = ColorTool().useCustomColorCodes(translation.status.embedStatusActivatedBody)
            }

            event.reply_("", listOf(activatedEmbed))
                .setEphemeral(true).queue()

            val embed = Embed {
                color = 0xf1c40f
                url = config.status.pageUrl
                title = ColorTool().useCustomColorCodes(
                    translation.status.embedMaintenanceOnTitle
                        .replace("%instance%", statusConstructor.instance.name)
                ).trimIndent()
                description = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnBody).trimIndent()
                field {
                    name = ColorTool().useCustomColorCodes(translation.status.embedMaintenanceOnReasonFieldName)
                        .trimIndent()
                    value =
                        ColorTool().useCustomColorCodes(reason).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        }
    }
}