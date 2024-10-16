package dev.vxrp.bot.commands.templates.support;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Objects;

public class Support {
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

    public static void pasteSupportTemplate(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translationManager.getString(TRANSLATIONS.SUPPORT.FIRST_TITLE))
                        .setDescription(translationManager.getString(TRANSLATIONS.SUPPORT.FIRST_BODY))
                        .setColor(Color.DARK_GRAY)
                        .build())
                .queue();
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translationManager.getString(TRANSLATIONS.SUPPORT.SECOND_TITLE))
                        .setDescription(translationManager.getString(TRANSLATIONS.SUPPORT.SECOND_BODY))
                        .setColor(Color.DARK_GRAY)
                        .build())
                .addActionRow(
                        Button.success("createNewTicket", "Create Support Ticket").withEmoji(Emoji.fromUnicode("📩")),
                        Button.danger("createNewUnban", "File Unban Request").withEmoji(Emoji.fromUnicode("📩"))
                ).queue();
    }
}
