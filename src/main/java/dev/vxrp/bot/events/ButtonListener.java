package dev.vxrp.bot.events;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.events.buttons.Rules;
import dev.vxrp.bot.events.buttons.Support;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonListener extends ListenerAdapter {
    public final Logger logger = LoggerFactory.getLogger(ButtonListener.class);
    TranslationManager translationManager = ScpTools.getTranslationManager();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Rules
        if (event.getComponentId().equals("paste_rules")) {
            Rules.paste(logger, event);
        }
        if (event.getComponentId().equals("update_rules")) {
            Rules.update(logger, event);
        }
        // Support
        if (event.getComponentId().equals("createNewTicket")) {
            Support.createSupportTicket(event, translationManager);
        }
        if (event.getComponentId().equals("createNewUnban")) {
            Support.createUnbanTicket(event, translationManager);
        }
    }
}
