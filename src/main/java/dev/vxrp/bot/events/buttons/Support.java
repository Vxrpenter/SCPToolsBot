package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.translations.groups.SupportGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class Support {
    private static final SupportGroup translations = LoadedConfigurations.getSupportTranslationMemoryLoad();

    public static void createSupportTicket(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("createNewTicket")) {
            event.replyModal(
                    Modal.create("supportTicket", translations.getModal_support_title())
                            .addComponents(
                                    ActionRow.of(shortModal(
                                            translations.getModal_support_first_title(),
                                            translations.getModal_support_first_placeholder()
                                    )),
                                    ActionRow.of(paragraphModal(
                                            translations.getModal_support_second_title(),
                                                    translations.getModal_support_second_placeholder())))
                            .build()).queue();
        }
    }

    public static void closeTicket(ButtonInteractionEvent event, User user) {
        if (event.getUser() == user) {
            event.reply("""
                    ```ansi
                    [2;31m[1;31mTask Denied  [0mYou cannot close a ticket you created
                    ```
                    """).setEphemeral(true).queue();
            return;
        }
        event.getMessageChannel().delete().queue();
    }

    public static void claimTicket(ButtonInteractionEvent event, User user) {
        if (event.getUser() == user) {
            event.reply("""
                    ```ansi
                    [2;31m[1;31mTask Denied  [0mYou cannot claim a ticket you created
                    ```
                    """).setEphemeral(true).queue();
            return;
        }

        List<ItemComponent> actionRow = event.getMessage().getActionRows().get(0).getComponents();
        actionRow.remove(1);
        actionRow.add(1, Button.primary("claim_support_ticket", "Claim Ticket").asDisabled());
        event.getMessage().editMessage(event.getMessage().getContentRaw()).setActionRow(actionRow).queue();

        event.replyEmbeds(new EmbedBuilder()
                .setDescription("""
                        This Ticket has been claimed by <@808746591829229601>
                        """)
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
