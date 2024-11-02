package dev.vxrp.bot.commands.templates.noticeOfDeparture;

import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.configuration.records.ButtonGroup;
import dev.vxrp.util.configuration.records.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class NoticeOfDeparture {
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();
    private static final ButtonGroup buttons = LoadedConfigurations.getButtonMemoryLoad();

    public static void pasteDeRegisterTemplate(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle(translations.first_title())
                        .setDescription(translations.first_body())
                        .setColor(Color.DARK_GRAY)
                        .build())
                .addActionRow(
                        Button.success("file_nod", buttons.file_notice_of_departure())
                )
                .queue();
    }
}