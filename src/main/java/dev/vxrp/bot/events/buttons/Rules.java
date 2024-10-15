package dev.vxrp.bot.events.buttons;

import org.slf4j.Logger;
import dev.vxrp.bot.commands.templates.rules.RulesTemplateUnit;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Objects;

public class Rules {
    public static void paste(Logger logger, ButtonInteractionEvent event) {
        event.reply("Pasting rules...").setEphemeral(true).queue();
        logger.warn("Rule pasting might take a while and the usage of the parser may create performance issues");
        RulesTemplateUnit.pasteRules(Objects.requireNonNull(event.getGuild()), event.getChannelId());
    }
    public static void update(Logger logger, ButtonInteractionEvent event) {
        RulesTemplateUnit.updateRules(Objects.requireNonNull(event.getGuild()),event.getChannelId(), event.getMessageId());
        logger.warn("The rules update feature is unstable and it's use is discouraged");
    }


}
