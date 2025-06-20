package dev.vxrp.bot.commands.handler.status.status

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.ConnectionTable
import dev.vxrp.util.color.ColorTool
import dev.vxrp.util.statusInstances
import dev.vxrp.util.statusMappedBots
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatusCommand(val config: Config, val translation: Translation) {
    fun changeMaintenanceState(event: SlashCommandInteractionEvent) {
        val currentPort = statusMappedBots[event.jda.selfUser.id]
        val currentMaintenance = ConnectionTable().queryFromTable(currentPort.toString()).maintenance == true

        if (currentMaintenance) {
            var reason: String = translation.status.embedMaintenanceOffReasonFieldValue
            if (event.getOption("reason")?.asString != null) {
                reason = event.getOption("reason")?.asString!!
            }

            ConnectionTable().setMaintenance(currentPort.toString(), false)
            val deactivatedEmbed = Embed {
                color = 0xE74D3C
                title = ColorTool().parse(translation.status.embedStatusDeactivatedTitle)
                description = ColorTool().parse(translation.status.embedStatusDeactivatedBody)
            }

            event.reply_("", listOf(deactivatedEmbed))
                .setEphemeral(true).queue()

            val embed = Embed {
                color = 0x2ECC70
                url = config.status.pageUrl
                title = ColorTool().parse(
                    translation.status.embedMaintenanceOffTitle
                        .replace("%instance%", statusInstances[currentPort]!!.name)
                ).trimIndent()
                description = ColorTool().parse(translation.status.embedMaintenanceOffBody).trimIndent()
                field {
                    name = ColorTool().parse(translation.status.embedMaintenanceOffReasonFieldName)
                        .trimIndent()
                    value =
                        ColorTool().parse(reason).trimIndent()
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
                title = ColorTool().parse(translation.status.embedStatusActivatedTitle)
                description = ColorTool().parse(translation.status.embedStatusActivatedBody)
            }

            event.reply_("", listOf(activatedEmbed))
                .setEphemeral(true).queue()

            val embed = Embed {
                color = 0xf1c40f
                url = config.status.pageUrl
                title = ColorTool().parse(
                    translation.status.embedMaintenanceOnTitle
                        .replace("%instance%", statusInstances[currentPort]!!.name)
                ).trimIndent()
                description = ColorTool().parse(translation.status.embedMaintenanceOnBody).trimIndent()
                field {
                    name = ColorTool().parse(translation.status.embedMaintenanceOnReasonFieldName)
                        .trimIndent()
                    value =
                        ColorTool().parse(reason).trimIndent()
                }
            }

            event.jda.getTextChannelById(config.status.postChannel)?.send("", listOf(embed))?.queue()
        }
    }
}