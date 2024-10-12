package dev.vxrp.bot.commands.templates;

import dev.vxrp.bot.support.Support;
import dev.vxrp.bot.util.Enmus.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TemplateCommand extends ListenerAdapter {
    public final static Logger logger = LoggerFactory.getLogger(TemplateCommand.class);
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getFullCommandName().equals("template")) return;
        if (event.getOption("template") == null) return;
        String template = Objects.requireNonNull(event.getOption("template")).getAsString();

        if (template.equals("rules")) {
            event.reply("Choose the rule pasting option")
                    .addActionRow(
                            Button.success("paste_rules", "Paste Rules"),
                            Button.danger("update_rules", "Update Rules")
                    )
                    .setEphemeral(true).queue();
            logger.info("User {} executed command template with args 'rules'", ColorTool.apply(DCColor.GREEN, event.getUser().getGlobalName()));
        }
        if (template.equals("support")) {
            Support.pasteSupportTemplate(Objects.requireNonNull(event.getGuild()), event.getChannelId());
            logger.info("User {} executed command template with args 'support'", ColorTool.apply(DCColor.GREEN, event.getUser().getGlobalName()));
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("paste_rules")) {
            RulesTemplateUnit.pasteRules(Objects.requireNonNull(event.getGuild()), event.getChannelId());
        }
        if (event.getComponentId().equals("update_rules")) {
            RulesTemplateUnit.updateRules(Objects.requireNonNull(event.getGuild()),event.getChannelId(), event.getMessageId());
            logger.warn("The rules update feature is unstable and it's use is discouraged");
        }
    }
}
