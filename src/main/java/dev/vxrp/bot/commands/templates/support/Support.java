package dev.vxrp.bot.commands.templates.support;

import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.SupportGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class Support {
    private static final SupportGroup translations = LoadedConfigurations.getSupportTranslationMemoryLoad();

    public static void pasteSupportTemplate(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(ColorTool.useCustomColorCodes(translations.first_title()))
                        .setDescription(ColorTool.useCustomColorCodes(translations.first_body()))
                        .setColor(Color.DARK_GRAY)
                        .build())
                .queue();
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(ColorTool.useCustomColorCodes(translations.second_title()))
                        .setDescription(ColorTool.useCustomColorCodes(translations.second_body()))
                        .setColor(Color.DARK_GRAY)
                        .build())
                .addActionRow(
                        Button.success("createNewTicket", "Create Support Ticket").withEmoji(Emoji.fromUnicode("📩")),
                        Button.danger("createNewUnban", "File Unban Request").withEmoji(Emoji.fromUnicode("📩"))
                ).queue();
    }
}
