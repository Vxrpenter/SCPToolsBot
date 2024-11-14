package dev.vxrp.bot.events;

import dev.vxrp.bot.events.modals.NoticeOfDeparture;
import dev.vxrp.bot.events.modals.Regulars;
import dev.vxrp.bot.events.modals.Support;
import dev.vxrp.bot.events.modals.Unban;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class ModalListener extends ListenerAdapter {
    public final Logger logger = LoggerFactory.getLogger(ModalListener.class);

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        // Support
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
            event.getJDA().retrieveUserById(event.getModalId().split(":")[1]).queue(user -> Unban.dismissUnban(event, user));
        }
        // Regulars
        if (event.getModalId().startsWith("regulars_data_modal")) {
            try {
                Regulars.regularDataModal(event);
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // Notice of Departure
        if (event.getModalId().equals("notice_of_departure")) {
            NoticeOfDeparture.createNewNoticeOfDeparture(event);
        }
        if (event.getModalId().startsWith("reason_action_accepted_nod")) {
            event.getJDA().retrieveUserById(event.getModalId().split(":")[1]).queue(user -> NoticeOfDeparture.acceptNoticeOfDeparture(event, user));
        }
        if (event.getModalId().startsWith("reason_action_dismiss_nod")) {
            event.getJDA().retrieveUserById(event.getModalId().split(":")[1]).queue(user -> {
                try {
                    NoticeOfDeparture.dismissNoticeOfDeparture(event, user);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if (event.getModalId().startsWith("reason_action_revoke_nod")) {
            event.getJDA().retrieveUserById(event.getModalId().split(":")[1]).queue(user -> {
                try {
                    NoticeOfDeparture.revokeNoticeOfDeparture(event, user);
                } catch (SQLException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
