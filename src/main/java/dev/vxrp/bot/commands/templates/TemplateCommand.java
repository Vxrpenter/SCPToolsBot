package dev.vxrp.bot.commands.templates;

import dev.vxrp.bot.commands.templates.noticeOfDeparture.NoticeOfDeparture;
import dev.vxrp.bot.commands.templates.support.Support;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.configuration.records.ButtonGroup;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TemplateCommand extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(TemplateCommand.class);
    private final ButtonGroup buttons = LoadedConfigurations.getButtonMemoryLoad();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getFullCommandName().equals("template")) return;
        if (event.getOption("template") == null) return;
        String template = Objects.requireNonNull(event.getOption("template")).getAsString();

        if (template.equals("rules")) {
            event.reply("Choose the rule pasting option")
                    .addActionRow(
                            Button.success("paste_rules", buttons.paste_rules()),
                            Button.danger("update_rules", buttons.update_rules())
                    )
                    .setEphemeral(true).queue();
        }
        if (template.equals("support")) {
            Support.pasteSupportTemplate(event);
        }
        if (template.equals("notice_of_departure")) {
            NoticeOfDeparture.pasteDeRegisterTemplate(event);
        }
        logger.info("User {} executed command template with args '{}'", ColorTool.apply(DCColor.GREEN, event.getUser().getGlobalName()), template);


    }
}
