package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.TRANSLATIONS;
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
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

    public static void createSupportTicket(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("createNewTicket")) {
            event.replyModal(
                    Modal.create("supportTicket", translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_TITLE))
                            .addComponents(
                                    ActionRow.of(shortModal(
                                            translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_TITLE),
                                            translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_PLACEHOLDER)
                                    )),
                                    ActionRow.of(paragraphModal(
                                            translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_TITLE),
                                            translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_PLACEHOLDER))))
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
