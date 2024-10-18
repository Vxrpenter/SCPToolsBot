package dev.vxrp.bot.commands.templates.noticeOfDeparture;

import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.translations.groups.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class NoticeOfDeparture {
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();

    public static void pasteDeRegisterTemplate(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translations.getFirst_title())
                        .setDescription(translations.getFirst_body())
                        .setColor(Color.DARK_GRAY)
                        .build())
                .addActionRow(
                        Button.success("file_nod", "File Notice of Departure")
                )
                .queue();
    }
}