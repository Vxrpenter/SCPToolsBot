package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.SupportGroup;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.Objects;

public class Unban {
    private static final SupportGroup translations = LoadedConfigurations.getSupportTranslationMemoryLoad();

    public static void createUnbanTicket(ButtonInteractionEvent event) {
        event.replyModal(
                Modal.create("unbanTicket", ColorTool.useCustomColorCodes(translations.modal_unban_title()))
                        .addComponents(
                                ActionRow.of(shortModal(
                                        "unban_steamID",
                                        ColorTool.useCustomColorCodes(translations.modal_unban_first_title()),
                                        ColorTool.useCustomColorCodes(translations.modal_unban_first_placeholder()),
                                        17, 17)),
                                ActionRow.of(shortModal(
                                        "unban_ban_reason",
                                        ColorTool.useCustomColorCodes(translations.modal_unban_second_title()),
                                        ColorTool.useCustomColorCodes(translations.modal_unban_second_placeholder()),
                                        5, 100)),
                                ActionRow.of(paragraphModal(
                                        ColorTool.useCustomColorCodes(translations.modal_unban_third_title()),
                                        ColorTool.useCustomColorCodes(translations.modal_unban_third_placeholder()))))
                        .build()).queue();
    }

    public static void dismissTicket(ButtonInteractionEvent event, User user) {
        String steamID = Objects.requireNonNull(event.getButton().getId()).split(":")[2];
        event.replyModal(
                Modal.create("reason_action_reason_dismiss:"+user.getId()+":"+steamID+":"+event.getMessageId()+":", ColorTool.useCustomColorCodes(translations.modal_reason_action_title()))
                        .addComponents(ActionRow.of(shortModal(
                                "reason_action_reason",
                                ColorTool.useCustomColorCodes(translations.modal_reason_action_first_title()),
                                ColorTool.useCustomColorCodes(translations.modal_reason_action_first_placeholder()),
                                10, 100
                        ))).build()).queue();
    }

    public static void acceptTicket(ButtonInteractionEvent event, User user) {
        String steamID = Objects.requireNonNull(event.getButton().getId()).split(":")[2];
        event.replyModal(
                Modal.create("reason_action_unban_accept:"+user.getId()+":"+steamID+":"+event.getMessageId()+":", ColorTool.useCustomColorCodes(translations.modal_reason_action_title()))
                        .addComponents(ActionRow.of(shortModal(
                                "reason_action_reason",
                                ColorTool.useCustomColorCodes(translations.modal_reason_action_first_title()),
                                ColorTool.useCustomColorCodes(translations.modal_reason_action_first_placeholder()),
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
