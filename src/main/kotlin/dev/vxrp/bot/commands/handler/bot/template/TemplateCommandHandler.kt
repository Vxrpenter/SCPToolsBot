package dev.vxrp.bot.commands.handler.bot.template

import dev.vxrp.bot.commands.handler.bot.template.templates.NoticeOfDepartureTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.RegularsTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.SupportTemplate
import dev.vxrp.bot.commands.handler.bot.template.templates.VerifyTemplate
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TemplateCommandHandler(val config: Config, private val translations:Translation) {
    fun findOption(event: SlashCommandInteractionEvent) {
        val option = event.getOption("template")?.asString

        when (option) {
            "support" -> {
                SupportTemplate(config, translations).pasteTemplate(event)
            }

            "verify" -> {
                VerifyTemplate(config, translations).pasteTemplate(event)
            }

            "notice_of_departure" -> {
                NoticeOfDepartureTemplate(config, translations).pasteTemplate(event)
            }

            "regulars" -> {
                RegularsTemplate(config, translations).pasteTemplate(event)
            }
        }
    }
}