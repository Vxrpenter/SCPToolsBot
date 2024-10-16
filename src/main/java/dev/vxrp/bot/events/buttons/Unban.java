package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.Objects;

public class Unban {
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

    public static void createUnbanTicket(ButtonInteractionEvent event) {
        event.replyModal(
                Modal.create("unbanTicket", translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_TITLE))
                        .addComponents(
                                ActionRow.of(shortModal(
                                        "unban_steamID",
                                        translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_TITLE),
                                        translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_PLACEHOLDER),
                                        17, 17)),
                                ActionRow.of(shortModal(
                                        "unban_ban_reason",
                                        translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_TITLE),
                                        translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_PLACEHOLDER),
                                        5, 100)),
                                ActionRow.of(paragraphModal(
                                        translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_TITLE),
                                        translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_PLACEHOLDER))))
                        .build()).queue();
    }

    public static void dismissTicket(ButtonInteractionEvent event, User user) {
        String steamID = Objects.requireNonNull(event.getButton().getId()).split(":")[2];
        event.replyModal(
                Modal.create("reason_action_reason_dismiss:"+user.getId()+":"+steamID+":"+event.getMessageId()+":", translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_TITLE))
                        .addComponents(ActionRow.of(shortModal(
                                "reason_action_reason",
                                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_TITLE),
                                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_PLACEHOLDER),
                                10, 100
                        ))).build()).queue();
    }

    public static void acceptTicket(ButtonInteractionEvent event, User user) {
        String steamID = Objects.requireNonNull(event.getButton().getId()).split(":")[2];
        event.replyModal(
                Modal.create("reason_action_unban_accept:"+user.getId()+":"+steamID+":"+event.getMessageId()+":", translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_TITLE))
                        .addComponents(ActionRow.of(shortModal(
                                "reason_action_reason",
                                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_TITLE),
                                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_PLACEHOLDER),
                                10, 100
                        ))).build()).queue();
    }

    private static TextInput shortModal(String id, String title, String placeholder, int minLenght, int maxLenght) {
        return TextInput.create(id, title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(minLenght)
                .setMaxLength(maxLenght)
                .build();
    }
    private static TextInput paragraphModal(String title, String placeholder) {
        return TextInput.create("unban_reasoning", title, TextInputStyle.PARAGRAPH)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMaxLength(1000)
                .build();
    }
}
