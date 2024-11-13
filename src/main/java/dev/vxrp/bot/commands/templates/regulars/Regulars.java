package dev.vxrp.bot.commands.templates.regulars;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.regulars.RegularsManager;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.translation.RegularsGroup;
import dev.vxrp.util.records.regular.Regular;
import dev.vxrp.util.records.regular.RegularConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Regulars {
    private static final RegularsGroup translations = (RegularsGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.REGULARS_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);

    public static void pasteRegularTemplate(SlashCommandInteractionEvent event) throws IOException {
        List<MessageEmbed> messages = new ArrayList<>();
        messages.add(new EmbedBuilder()
                .setTitle(translations.first_tile())
                .setDescription(translations.first_body())
                .setColor(Color.BLACK)
                .build());

        for (String group : getStrings()) {
            messages.add(new EmbedBuilder()
                    .setDescription(group)
                    .build());
        }

        event.reply("Pasted regulars template").setEphemeral(true).queue();
        event.getChannel().sendMessageEmbeds(messages)
                .addActionRow(
                        Button.success("open_personal_regular_menu", buttons.open_regular_menu()).withEmoji(Emoji.fromUnicode("⭐"))
                ).queue();
    }

    @NotNull
    private static List<String> getStrings() throws IOException {
        List<String> groups = new ArrayList<>();
        RegularsManager manager = ScpTools.getRegularsManager();

        for (Regular regular : manager.getRegulars()) {
            String product;

            String group_role = "<@&"+regular.id()+">";
            if (!regular.use_custom_role()) group_role = "**None**";

            product = translations.group_template()
                    .replace("%group%", regular.name())
                    .replace("%description%", regular.description())
                    .replace("%group_role%", group_role);

            StringBuilder builder = new StringBuilder();
            for (RegularConfig config : manager.getSingleConfig(regular)) {
                String role = translations.role_template()
                        .replace("%role%", "<@&"+config.id()+">")
                        .replace("%description%", config.description())
                        .replace("%timeframe%", String.valueOf(config.playtime_requirements()));

                builder.append(role);
            }

            groups.add(product.replace("%roles%", builder.toString()));
        }
        return groups;
    }
}
