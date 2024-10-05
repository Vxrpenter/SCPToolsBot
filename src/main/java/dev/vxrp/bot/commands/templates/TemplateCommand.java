package dev.vxrp.bot.commands.templates;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Objects;

public class TemplateCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("template")) return;
        if (event.getOption("template") == null) return;
        String template = Objects.requireNonNull(event.getOption("template")).getAsString();

        if (template.equals("rules")) {
            event.reply("Choose the rule pasting option")
                    .addActionRow(
                            Button.success("paste_rules", "Paste Rules"),
                            Button.danger("update_rules", "Update Rules")
                    )
                    .setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("paste_rules")) {
            RulesTemplateUnit.pasteRules(Objects.requireNonNull(event.getGuild()), event.getChannelId());
        }
        if (event.getComponentId().equals("update_rules")) {
            RulesTemplateUnit.updateRules(Objects.requireNonNull(event.getGuild()), event.getMessageId() ,event.getChannelId());
        }
    }
}
