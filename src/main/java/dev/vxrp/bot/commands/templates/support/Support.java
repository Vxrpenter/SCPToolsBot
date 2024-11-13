package dev.vxrp.bot.commands.templates.support;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.translation.SupportGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class Support {
    private static final SupportGroup translations = (SupportGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.SUPPORT_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);

    public static void pasteSupportTemplate(SlashCommandInteractionEvent event) {
        event.reply("Pasted support template").setEphemeral(true).queue();
        event.getChannel().sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translations.first_title())
                        .setDescription(translations.first_body())
                        .setColor(Color.DARK_GRAY)
                        .build(),
                new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translations.second_title())
                        .setDescription(translations.second_body())
                        .setColor(Color.DARK_GRAY)
                        .build())
                .addActionRow(
                        Button.success("createNewTicket", buttons.create_new_support_ticket()).withEmoji(Emoji.fromUnicode("📩")),
                        Button.danger("createNewUnban", buttons.create_new_unban_ticket()).withEmoji(Emoji.fromUnicode("📩"))
                ).queue();
    }
}