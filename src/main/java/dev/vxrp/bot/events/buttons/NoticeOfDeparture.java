package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.NoticeOfDepartureGroup;
import dev.vxrp.bot.util.configuration.groups.SupportGroup;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class NoticeOfDeparture {
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();
    private static final SupportGroup reason_action = LoadedConfigurations.getSupportTranslationMemoryLoad();

    public static void createNoticeOfDeparture(ButtonInteractionEvent event) {
        event.replyModal(
                Modal.create("notice_of_departure", translations.modal_title())
                        .addComponents(
                                ActionRow.of(shortModal(
                                        "nod_timeframe",
                                        translations.modal_first_title(),
                                        translations.modal_first_placeholder(),
                                        8,10
                                )),
                                ActionRow.of(paragraphModal(
                                        translations.modal_second_title(),
                                        translations.modal_second_placeholder()
                                )))
                        .build()).queue();
    }

    public static void dismissNoticeOfDeparture(ButtonInteractionEvent event, User user) {
        event.replyModal(
                Modal.create("reason_action_dismiss_nod:"+user.getId()+":", reason_action.modal_reason_action_title())
                        .addComponents(ActionRow.of(shortModal(
                                "reason_action_reason",
                                reason_action.modal_reason_action_first_title(),
                                reason_action.modal_reason_action_first_placeholder(),
                                10, 100
                        ))).build()).queue();
    }

    private static TextInput shortModal(String id,String title, String placeholder, int min, int max) {
        return TextInput.create(id, title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
    }
    private static TextInput paragraphModal(String title, String placeholder) {
        return TextInput.create("nod_reason", title, TextInputStyle.PARAGRAPH)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMaxLength(1000)
                .build();
    }
}
