package dev.vxrp.bot.commands.handler.bot.template

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
                if (!PermissionManager(config, translation).checkStatus(event.hook, StatusMessageType.TEMPLATE, config.settings.verify.active)) return
                VerifyTemplate(config, translation).pasteTemplate(event)
            }

            "notice_of_departure" -> {
                if (!PermissionManager(config, translation).checkStatus(event.hook, StatusMessageType.TEMPLATE, config.settings.noticeOfDeparture.active)) return
                NoticeOfDepartureTemplate(config, translation).pasteTemplate(event)
            }

            "regulars" -> {
                if (!PermissionManager(config, translation).checkStatus(event.hook, StatusMessageType.TEMPLATE, config.settings.regulars.active)) return
                RegularsTemplate(config, translation).pasteTemplate(event)
            }
        }
    }
}