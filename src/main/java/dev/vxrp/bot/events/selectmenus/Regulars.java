package dev.vxrp.bot.events.selectmenus;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.translation.RegularsGroup;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class Regulars {
    private static final RegularsGroup regulars = (RegularsGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.REGULARS_GROUP);

    public static void syncRegularsEnterInfo(StringSelectInteractionEvent event) {
        event.replyModal(
                Modal.create("regulars_data_modal:"+event.getValues().getFirst()+":", regulars.data_modal_title())
                        .addComponents(
                                ActionRow.of(shortModal(
                                        "regular_steamId",
                                        regulars.data_modal_first_title(),
                                        regulars.data_modal_first_placeholder(),
                                        17, 17
                                )),
                                ActionRow.of(shortModal(
                                        "regular_username",
                                        regulars.data_modal_second_title(),
                                        regulars.data_modal_second_placeholder(),
                                        1, 100)))
                        .build()).queue();
    }

    private static TextInput shortModal(String id, String title, String placeholder, int min, int max) {
        return TextInput.create(id, title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
    }
}
