package dev.vxrp.bot.commands;

import dev.vxrp.bot.commands.help.HelpCommand;
import dev.vxrp.bot.commands.templates.TemplateCommand;
import dev.vxrp.util.Enums.DCColor_DEPRECATED;
import dev.vxrp.util.colors.ColorTool;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CommandListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(CommandListener.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String template = Objects.requireNonNull(event.getOption("template")).getAsString();

        // Help
        if (event.getFullCommandName().equals("help")) {
            HelpCommand.pasteHelp(event);
        }

        // Template
        if (event.getFullCommandName().equals("template") && template.equals("rules")) {
            TemplateCommand.templateRules(event);
        }
        if (template.equals("support")) {
            TemplateCommand.templateSupport(event);
        }

        if (template.equals("notice_of_departure")) {
            TemplateCommand.templateNoticeOfDeparture(event);
        }
        if  (template.equals("regulars")) {
            TemplateCommand.templateRegulars(event);
        }
        logger.info("User {} executed command {} with args '{}'", ColorTool.apply(DCColor_DEPRECATED.GREEN, event.getUser().getGlobalName()), event.getFullCommandName() , template);
    }
}
