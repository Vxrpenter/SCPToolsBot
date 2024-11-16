package dev.vxrp.bot.events.buttons;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.translation.RegularsGroup;
import dev.vxrp.util.records.regular.Regular;
import dev.vxrp.util.records.regular.RegularMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

public class Regulars {
    private static final RegularsGroup regulars = (RegularsGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.REGULARS_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);

    public static void openRegularMenu(ButtonInteractionEvent event) throws SQLException {
        String group = "None";
        String role = "None";
        String time = "0.00h";

        boolean exists = ScpTools.getSqliteManager().getRegularsTableManager().exists(event.getUser().getId());
        boolean deactivated = false;

        if (exists) {
            RegularMember member = ScpTools.getSqliteManager().getRegularsTableManager().getRegularMember(event.getUser().getId());
            deactivated = member.deactivated();
            group = "<@&"+member.group_role()+">";
            role = "<@&"+member.role()+">";
            time = String.valueOf(Math.round(member.time() / 3600));
        }

        event.replyEmbeds(new EmbedBuilder()
                .setTitle(regulars.second_title())
                .setDescription(regulars.second_body()
                        .replace("%group%", group.replace("<@&null>", "None"))
                        .replace("%role%", role.replace("<@&null>", "None"))
                        .replace("%time%", time)
                )
                .setColor(Color.BLACK)
                .build()).setActionRow(actionRow(exists, deactivated)).setEphemeral(true).queue();
    }

    public static void syncRegulars(ButtonInteractionEvent event) throws IOException {
        boolean groups = false;
        Collection<SelectOption> options = new ArrayList<>();
        for (Regular regular : ScpTools.getRegularsManager().getRegulars()) {
            if (regular.use_custom_role()) {
                groups = true;

                Role role = Objects.requireNonNull(event.getGuild()).getRoleById(regular.id());

                List<Emoji> emojis = List.of(Emoji.fromFormatted("⭐"), Emoji.fromFormatted("🎉"), Emoji.fromFormatted("🤡"), Emoji.fromFormatted("🗿"), Emoji.fromFormatted("🍕"));

                assert role != null;
                Random random = new Random();
                options.add(SelectOption.of(role.getName(), role.getId()).withEmoji(emojis.get(random.nextInt(emojis.size()))));
            }
        }

        if (groups) {
            event.reply(regulars.sync_group_select_message())
                    .addActionRow(StringSelectMenu.create("regulars_sync_group_select")
                            .addOptions(options)
                            .setMaxValues(1)
                            .setMinValues(1)
                            .setPlaceholder("Choose your group")
                            .build()).setEphemeral(true).queue();
        } else {
            event.replyModal(
                    Modal.create("regulars_data_modal", regulars.data_modal_title())
                            .addComponents(
                                    ActionRow.of(shortModal(
                                            regulars.data_modal_first_title(),
                                            regulars.data_modal_first_placeholder()
                                    )))
                            .build()).queue();
        }
    }

    public static void deactivateSyncRegulars(ButtonInteractionEvent event) throws SQLException {
        ScpTools.getSqliteManager().getRegularsTableManager().updateDeactivated(event.getUser().getId(), true);
        event.getMessage().delete().queue();
        openRegularMenu(event);
        event.getHook().sendMessage(regulars.sync_deactivated()).setEphemeral(true).queue();
    }

    public static void reactivateRegulars(ButtonInteractionEvent event) throws SQLException {
        ScpTools.getSqliteManager().getRegularsTableManager().updateDeactivated(event.getUser().getId(), false);
        event.getMessage().delete().queue();
        openRegularMenu(event);
        event.getHook().sendMessage(regulars.sync_reactivated()).setEphemeral(true).queue();
    }

    public static void removeSyncRegularsConfirmation(ButtonInteractionEvent event) {
        event.reply(regulars.sync_remove_confirm_message())
                .addActionRow(
                        Button.danger("remove_sync_regular_confirm:"+event.getMessageId()+":", buttons.remove_sync_regulars())
                )
                .setEphemeral(true).queue();
    }

    public static void removeSyncRegulars(ButtonInteractionEvent event) throws SQLException, InterruptedException {
        event.getMessage().delete().queue();
        ScpTools.getSqliteManager().getRegularsTableManager().deleteRegular(event.getUser().getId());
        if (ScpTools.getSqliteManager().getQueueManager().exists(event.getUser().getId())) {
            ScpTools.getSqliteManager().getQueueManager().deleteAction(event.getUser().getId());
        }
        openRegularMenu(event);
        event.getHook().sendMessage(regulars.sync_removed_message()).setEphemeral(true).queue();
    }

    private static TextInput shortModal(String title, String placeholder) {
        return TextInput.create("regular_steamId", title, TextInputStyle.SHORT)
                .setPlaceholder(placeholder)
                .setRequired(true)
                .setMinLength(17)
                .setMaxLength(17)
                .build();
    }

    private static Collection<? extends ItemComponent> actionRow(boolean exits, boolean deactivated) {
        Collection<ItemComponent> rows = new ArrayList<>();
        if (deactivated && exits) {
            rows.add(Button.success("regular_sync", buttons.sync_regulars()).withEmoji(Emoji.fromUnicode("🔥")).asDisabled());
            rows.add(Button.success("regular_reactivate", buttons.reactivate_sync_regulars()).withEmoji(Emoji.fromUnicode("⭐")));
            rows.add(Button.danger("regular_deactivate", buttons.deactivate_sync_regulars()).withEmoji(Emoji.fromUnicode("🫷")).asDisabled());
            rows.add(Button.danger("regular_remove", buttons.remove_sync_regulars()).withEmoji(Emoji.fromUnicode("🧯")));
        }
        if (!deactivated && exits) {
            rows.add(Button.success("regular_sync", buttons.sync_regulars()).withEmoji(Emoji.fromUnicode("🔥")).asDisabled());
            rows.add(Button.danger("regular_deactivate", buttons.deactivate_sync_regulars()).withEmoji(Emoji.fromUnicode("🫷")));
            rows.add(Button.danger("regular_remove", buttons.remove_sync_regulars()).withEmoji(Emoji.fromUnicode("🧯")));
        }
        if (!exits) {
            rows.add(Button.success("regular_sync", buttons.sync_regulars()).withEmoji(Emoji.fromUnicode("🔥")));
            rows.add(Button.danger("regular_deactivate", buttons.deactivate_sync_regulars()).withEmoji(Emoji.fromUnicode("🫷")));
            rows.add(Button.danger("regular_remove", buttons.remove_sync_regulars()).withEmoji(Emoji.fromUnicode("🧯")).asDisabled());
        }
        return rows;
    }
}
