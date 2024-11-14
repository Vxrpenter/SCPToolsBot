package dev.vxrp.bot.commands.templates.noticeOfDeparture;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.translation.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class NoticeOfDeparture {
    private static final NoticeOfDepartureGroup translations = (NoticeOfDepartureGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.NOTICE_OF_DEPARTURE_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);

    public static void pasteDeRegisterTemplate(SlashCommandInteractionEvent event) {
        event.reply("Pasted regulars template").setEphemeral(true).queue();
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