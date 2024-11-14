package dev.vxrp.bot.events;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.commands.help.HelpCommand;
import dev.vxrp.bot.events.buttons.*;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class ButtonListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(ButtonListener.class);
    private final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Help
        if (event.getComponentId().startsWith("help_first_page")) {
            event.deferEdit().queue(_ -> event.getChannel().editMessageEmbedsById(event.getMessageId(), HelpCommand.pages().getFirst())
                    .setActionRow(HelpCommand.actionRow(0)).queue());
        }
        if (event.getComponentId().startsWith("help_last_page")) {
            event.deferEdit().queue(_ -> event.getChannel().editMessageEmbedsById(event.getMessageId(), HelpCommand.pages().getLast())
                    .setActionRow(HelpCommand.actionRow(5)).queue());
        }

        if (event.getComponentId().startsWith("help_go_back")) {
            int page = Integer.parseInt(event.getComponentId().split(":")[1])-1;
            event.deferEdit().queue(_ -> event.getChannel().editMessageEmbedsById(event.getMessageId(), HelpCommand.pages().get(page))
                    .setActionRow(HelpCommand.actionRow(page)).queue());
        }
        if (event.getComponentId().startsWith("help_go_forward")) {
            int page = Integer.parseInt(event.getComponentId().split(":")[1])+1;
            event.deferEdit().queue(_ -> event.getChannel().editMessageEmbedsById(event.getMessageId(), HelpCommand.pages().get(page))
                    .setActionRow(HelpCommand.actionRow(page)).queue());
        }

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
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> {
                try {
                    Support.closeTicket(event, user);
                } catch (SQLException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if (event.getComponentId().startsWith("claim_support_ticket")) {
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> {
                try {
                    Support.claimTicket(event, user);
                } catch (SQLException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if (event.getComponentId().equals("settings_support_ticket")) {
            //WIP
            event.reply("Feature currently under development").setEphemeral(true).queue();
        }

        if (event.getComponentId().equals("createNewUnban")) {
            if (config.cedmod_active()) {
                Unban.createUnbanTicket(event);
            } else {
                event.reply("Cedmod compatibility is not active, so this template is disabled").setEphemeral(true).queue();
                logger.warn("An action called createNewUnban was cancelled do to cedmod compatibility not being active");
            }
        }
        if (event.getComponentId().startsWith("accept_unban_ticket")) {
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> Unban.acceptTicket(event, user));
        }
        if (event.getComponentId().startsWith("dismiss_unban_ticket")) {
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> Unban.dismissTicket(event, user));
        }
        if (event.getComponentId().equals("settings_unban_ticket")) {
            //WIP
            event.reply("Feature currently under development").setEphemeral(true).queue();
        }
        // Regulars
        if (event.getComponentId().equals("open_personal_regular_menu")) {
            try {
                Regulars.openRegularMenu(event);
            } catch (SQLException e) {
                logger.error("Could not open regular menu {}", e.getMessage());
            }
        }
        if (event.getComponentId().equals("regular_sync")) {
            try {
                Regulars.syncRegulars(event);
            } catch (IOException e) {
                logger.error("Could not check for regular groups {}", e.getMessage());
            }
        }
        if (event.getComponentId().equals("regular_reactivate")) {
            try {
                Regulars.reactivateRegulars(event);
            } catch (SQLException e) {
                logger.error("Could not correctly reactivate regular {}", e.getMessage());
            }
        }
        if (event.getComponentId().equals("regular_deactivate")) {
            try {
                Regulars.deactivateSyncRegulars(event);
            } catch (SQLException e) {
                logger.error("Could not correctly deactivate regular {}", e.getMessage());
            }
        }
        if (event.getComponentId().equals("regular_remove")) {
            Regulars.removeSyncRegularsConfirmation(event);
        }
        if (event.getComponentId().startsWith("remove_sync_regular_confirm")) {
            try {
                Regulars.removeSyncRegulars(event);
            } catch (SQLException | InterruptedException e) {
                logger.error("Could not correctly remove sync regular {}", e.getMessage());
            }
        }
        //Notice of Departure
        if (event.getComponentId().equals("file_nod")) {
            NoticeOfDeparture.createNoticeOfDeparture(event);
        }
        if (event.getComponentId().startsWith("accept_ticket_notice_of_departure")) {
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> NoticeOfDeparture.acceptedNoticeOfDeparture(event, user));
        }
        if (event.getComponentId().startsWith("dismiss_ticket_notice_of_departure")) {
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> NoticeOfDeparture.dismissNoticeOfDeparture(event, user));
        }
        if (event.getComponentId().startsWith("revoke_notice_of_departure")) {
            event.getJDA().retrieveUserById(event.getComponentId().split(":")[1]).queue(user -> NoticeOfDeparture.revokeNoticeOfDeparture(event, user));
        }
        if (event.getComponentId().equals("delete_notice_of_departure")) {
            NoticeOfDeparture.deleteNoticeOfDeparture(event);
        }
    }
}
