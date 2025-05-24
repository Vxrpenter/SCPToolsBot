package dev.vxrp.bot.commands.handler.bot.template

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.commands.handler.bot.template.templates.NoticeOfDepartureTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.RegularsTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.SupportTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.VerifyTemplate
import dev.vxrp.bot.permissions.PermissionManager
import dev.vxrp.bot.permissions.enums.StatusMessageType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TemplateCommandHandler(val config: Config, private val translation: Translation) {
    fun findOption(event: SlashCommandInteractionEvent) {
        val option = event.getOption("template")?.asString

        when (option) {
            "support" -> {
                SupportTemplate(config, translation).pasteTemplate(event)
            }

            "verify" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: VerifyTemplate(config, translation).pasteTemplate(event)
            }

            "notice_of_departure" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.settings.noticeOfDeparture.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: NoticeOfDepartureTemplate(config, translation).pasteTemplate(event)
            }

            "regulars" -> {
                PermissionManager(config, translation).checkStatus(StatusMessageType.TEMPLATE, config.settings.regulars.active, config.settings.verify.active, config.settings.webserver.active)?.let { embed ->
                    event.reply_("", listOf(embed)).setEphemeral(true).queue()
                } ?: RegularsTemplate(config, translation).pasteTemplate(event)
            }
        }
    }
}