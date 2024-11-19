package dev.vxrp.bot.commands.templates;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.commands.templates.noticeOfDeparture.NoticeOfDeparture;
import dev.vxrp.bot.commands.templates.regulars.Regulars;
import dev.vxrp.bot.commands.templates.support.Support;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TemplateCommand extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(TemplateCommand.class);
    private final static ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);
    private final static ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    private final static String noCedmodError = "Cedmod compatibility is not active, so this template is disabled";


    public static void templateRules(SlashCommandInteractionEvent event) {
        event.reply("Choose the rule pasting option")
                .addActionRow(
                        Button.success("paste_rules", buttons.paste_rules()),
                        Button.danger("update_rules", buttons.update_rules())
                )
                .setEphemeral(true).queue();
    }
    public static void templateSupport(SlashCommandInteractionEvent event) {
        Support.pasteSupportTemplate(event);
    }
    public static void templateNoticeOfDeparture(SlashCommandInteractionEvent event) {
        if (config.cedmod_active()) {
            NoticeOfDeparture.pasteDeRegisterTemplate(event);
        } else {
            event.reply(noCedmodError).setEphemeral(true).queue();
            logger.warn("An action of /template notice_of_departure was cancelled do to cedmod compatibility not being active");
        }
    }
    public static void templateRegulars(SlashCommandInteractionEvent event) {
        if (config.cedmod_active()) {
            try {
                Regulars.pasteRegularTemplate(event);
            } catch (IOException e) {
                logger.error("Ran into error while pasting regular template {}", e.getMessage());
            }
        } else {
            event.reply(noCedmodError).setEphemeral(true).queue();
            logger.warn("An action of /template regulars was cancelled do to cedmod compatibility not being active");
        }
    }
}
