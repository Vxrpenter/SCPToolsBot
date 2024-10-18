package dev.vxrp.bot.events;

import dev.vxrp.bot.events.modals.Support;
import dev.vxrp.bot.events.modals.Unban;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModalListener extends ListenerAdapter {
    public final Logger logger = LoggerFactory.getLogger(ModalListener.class);

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("supportTicket")) {
            Support.createSupportTicket(event, logger);
        }
        if (event.getModalId().equals("unbanTicket")) {
            Unban.createUnbanTicket(event, logger);
        }

        if (event.getModalId().startsWith("reason_action_unban_accept")) {
            try {
                Unban.acceptUnban(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getModalId().startsWith("reason_action_reason_dismiss")) {
            Unban.dismissUnban(event);
        }
    }
}
