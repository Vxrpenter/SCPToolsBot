package dev.vxrp.bot.regulars

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

class RegularsMessageHandler(val api: JDA, val config: Config, val translation: Translation) {
    fun sendRegulars(channel: TextChannel) {
        val embeds = mutableListOf<MessageEmbed>()

        val embed = Embed {
            title = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateTitle)
            description = ColorTool().useCustomColorCodes(translation.regulars.embedTemplateBody)
        }
        embeds.add(embed)

        val regulars = RegularsManager(config, translation).query()
        for (regular in regulars) {
            val stringBuilder: StringBuilder = StringBuilder()

            for (role in regular.config.roles) {
                stringBuilder.append(ColorTool().useCustomColorCodes(translation.regulars.embedTemplateRoleBody
                    .replace("%role%", role.id)
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

        channel.send("", embeds).queue()
    }
}