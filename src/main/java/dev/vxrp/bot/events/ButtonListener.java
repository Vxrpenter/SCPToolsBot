package dev.vxrp.bot.events;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.events.buttons.Rules;
import dev.vxrp.bot.events.buttons.Support;
import dev.vxrp.bot.events.buttons.Unban;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonListener extends ListenerAdapter {
    public final Logger logger = LoggerFactory.getLogger(ButtonListener.class);

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
            Support.createSupportTicket(event);
        }
        if (event.getComponentId().startsWith("close_support_ticket")) {
            User user = event.getJDA().getUserById(event.getComponentId().split(":")[1]);
            Support.closeTicket(event, user);
        }
        if (event.getComponentId().startsWith("claim_support_ticket")) {
            User user = event.getJDA().getUserById(event.getComponentId().split(":")[1]);
            Support.claimTicket(event, user);
        }
        if (event.getComponentId().equals("settings_support_ticket")) {
            //WIP
            event.reply("Feature currently under development").queue();
        }

        if (event.getComponentId().equals("createNewUnban")) {
            Unban.createUnbanTicket(event);
        }
        if (event.getComponentId().startsWith("accept_support_ticket")) {
            User user = event.getJDA().getUserById(event.getComponentId().split(":")[1]);
            if (user != null) {
                Unban.acceptTicket(event, user);
            }
        }
        if (event.getComponentId().startsWith("dismiss_unban_ticket")) {
            User user = event.getJDA().getUserById(event.getComponentId().split(":")[1]);
            if (user != null) {
                Unban.dismissTicket(event, user);
            }
        }
        if (event.getComponentId().equals("settings_unban_ticket")) {
            //WIP
            event.reply("Feature currently under development").queue();

        }
    }
}
