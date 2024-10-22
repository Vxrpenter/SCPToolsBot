package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class NoticeOfDeparture {
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();

    public static void createNoticeOfDeparture(ButtonInteractionEvent event) {
        event.replyModal(
                Modal.create("notice_of_departure", translations.modal_title())
                        .addComponents(
                                ActionRow.of(shortModal(
                                        translations.modal_first_title(),
                                        translations.modal_first_placeholder()
                                )),
                                ActionRow.of(paragraphModal(
                                        translations.modal_second_title(),
                                        translations.modal_second_placeholder()
                                )))
                        .build()).queue();
    }

    private static TextInput shortModal(String title, String placeholder) {
        return TextInput.create("nod_timeframe", title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(8)
                .setMaxLength(10)
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
