package dev.vxrp.bot.commands.templates.noticeOfDeparture;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class NoticeOfDeparture {
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

    public static void pasteDeRegisterTemplate(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_TITLE))
                        .setDescription(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_BODY))
                        .setColor(Color.DARK_GRAY)
                        .build())
                .addActionRow(
                        Button.success("file_nod", "File Notice of Departure")
                )
                .queue();
    }
}