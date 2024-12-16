package dev.vxrp.bot.commands.commanexecutes.template

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TemplateCommand(val config: Config, val translations:Translation) {
    fun findOption(event: SlashCommandInteractionEvent) {
        val option = event.getOption("template")?.asString

        when (option) {
            "rules" -> {
                RulesTemplate()
            }

            "support" -> {
                SupportTemplate(config, translations).pastePaste(event)
            }

            "notice_of_departure" -> {
                NoticeOfDepartureTemplate()
            }

            "regulars" -> {
                RegularsTemplate()
            }
        }
    }
}