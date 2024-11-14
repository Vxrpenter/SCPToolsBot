package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.LoggingGroup;
import dev.vxrp.util.configuration.records.translation.SupportGroup;
import dev.vxrp.util.logger.LoggerManager;
import dev.vxrp.util.records.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Support {
    private static final SupportGroup translations = (SupportGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.SUPPORT_GROUP);
    private static final LoggingGroup logging = (LoggingGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.LOGGING_GROUP);
    private static final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);

    public static void createSupportTicket(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("createNewTicket")) {
            event.replyModal(
                    Modal.create("supportTicket", translations.modal_support_title())
                            .addComponents(
                                    ActionRow.of(shortModal(
                                            translations.modal_support_first_title(),
                                            translations.modal_support_first_placeholder()
                                    )),
                                    ActionRow.of(paragraphModal(
                                            translations.modal_support_second_title(),
                                            translations.modal_support_second_placeholder())))
                            .build()).queue();
        }
    }

    public static void closeTicket(ButtonInteractionEvent event, User user) throws SQLException, InterruptedException {
        if (event.getUser() == user) {
            event.reply("""
                    ```ansi
                    [2;31m[1;31mTask Denied  [0mYou cannot close a ticket you created
                    ```
                    """).setEphemeral(true).queue();
            return;
        }

        Ticket ticket = ScpTools.getSqliteManager().getTicketsTableManager().getTicket(event.getMessage().getChannelId());
        new LoggerManager(event.getJDA()).closeLog(event.getUser(),
                logging.support_ticket_close_logging_action()
                        .replace("%id%", ticket.id())
                        .replace("%user%", "<@"+event.getUser().getId()+">")
                        .replace("%type%", ticket.identifier().toString())
                        .replace("%creator%", "<@"+ticket.creatorId()+">")
                        .replace("%handler%", "<@"+ticket.handlerId()+">").replace("<@null>", "None")
                        .replace("%state%", "OPEN")
                        .replace("%date%", ticket.creation_date()),
                config.ticket_logging_channel_id(), Color.RED);

        ScpTools.getSqliteManager().getTicketsTableManager().deleteTicket(event.getChannelId());
        event.getMessageChannel().delete().queue();
    }

    public static void claimTicket(ButtonInteractionEvent event, User user) throws SQLException, InterruptedException {
        if (event.getUser() == user) {
            event.reply("""
                    ```ansi
                    [2;31m[1;31mTask Denied  [0mYou cannot claim a ticket you created
                    ```
                    """).setEphemeral(true).queue();
            return;
        }

        List<ItemComponent> actionRow = event.getMessage().getActionRows().getFirst().getComponents();
        actionRow.remove(1);
        actionRow.add(1, Button.primary("claim_support_ticket", "Claim Ticket").asDisabled());
        event.getMessage().editMessage(event.getMessage().getContentRaw()).setActionRow(actionRow).queue();

        ScpTools.getSqliteManager().getTicketsTableManager().updateHandler(event.getChannelId(), event.getUser().getId());
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("""
                        This Ticket has been claimed by %user%
                        """
                        .replace("%user%", "<@"+event.getUser().getId()+">")
                )
                .build()).queue();
    }

    private static TextInput shortModal(String title, String placeholder) {
        return TextInput.create("support_subject", title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(5)
                .setMaxLength(100)
                .build();
    }
    private static TextInput paragraphModal(String title, String placeholder) {
        return TextInput.create("support_body", title, TextInputStyle.PARAGRAPH)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMaxLength(1000)
                .build();
    }
}
