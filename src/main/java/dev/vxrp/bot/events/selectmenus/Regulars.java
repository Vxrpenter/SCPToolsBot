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
                                        regulars.data_modal_first_title(),
                                        regulars.data_modal_first_placeholder()
                                )))
                        .build()).queue();
    }

    private static TextInput shortModal(String title, String placeholder) {
        return TextInput.create("regular_steamId", title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(17)
                .setMaxLength(17)
                .build();
    }
}
