package dev.vxrp.bot.commands.templates.deregistration;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class DeRegistration {
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

    public static void pasteDeRegisterTemplate(SlashCommandInteractionEvent event) {
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

                ).queue();
    }
}
