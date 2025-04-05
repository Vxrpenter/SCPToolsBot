package dev.vxrp.bot.commands.commanexecutes.template

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TemplateCommand(val config: Config, val translations:Translation) {
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