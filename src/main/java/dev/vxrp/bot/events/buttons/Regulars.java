package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.translation.RegularsGroup;
import dev.vxrp.util.records.regular.RegularMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class Regulars {
    private static final RegularsGroup regulars = (RegularsGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.REGULARS_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);

    public static void openRegularMenu(ButtonInteractionEvent event) throws SQLException {
        String group = "None";
        String role = "None";
        String time = "0.00h";

        if (ScpTools.getSqliteManager().getRegularsTableManager().exists(event.getUser().getId())) {
            RegularMember member = ScpTools.getSqliteManager().getRegularsTableManager().getRegularMember(event.getUser().getId());
            group = "<@&"+member.group_role()+">";
            role = "<@&"+member.role()+">";
            time = Math.round(member.time() / 1000) +"h";
        }

        event.replyEmbeds(new EmbedBuilder()
                .setTitle(regulars.second_title())
                .setDescription(regulars.second_body()
                        .replace("%group%", group)
                        .replace("%role%", role)
                        .replace("%time%", time)
                )
                .setColor(Color.BLACK)
                .build()).addActionRow(
                    Button.success("regular_sync", buttons.sync_regulars()).withEmoji(Emoji.fromUnicode("🔥")),
                    Button.danger("regular_deactivate", buttons.deactivate_sync_regulars()).withEmoji(Emoji.fromUnicode("🫷")),
                    Button.danger("regular_remove", buttons.remove_sync_regulars()).withEmoji(Emoji.fromUnicode("🧯"))
                ).setEphemeral(true).queue();
    }

    public static void syncRegulars(ButtonInteractionEvent event) {

    }

    public static void deactivateSyncRegularsConfirmation(ButtonInteractionEvent event) {

    }

    public static void deactivateSyncRegulars(ButtonInteractionEvent event) {

    }

    public static void removeSyncRegulars(ButtonInteractionEvent event) {

    }
}
