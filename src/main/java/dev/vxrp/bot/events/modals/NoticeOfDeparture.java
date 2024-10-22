package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.builder.StatsBuilder;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.ConfigGroup;
import dev.vxrp.bot.util.configuration.groups.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class NoticeOfDeparture {
    private static final ConfigGroup configs = LoadedConfigurations.getConfigMemoryLoad();
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();

    public static void createNewNoticeOfDeparture(ModalInteractionEvent event) {
        TextChannel channel = Objects.requireNonNull(event.getGuild()).getTextChannelById(configs.notice_of_departure_decision_channel_id());
        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        String[] givenDate = Objects.requireNonNull(event.getValue("nod_timeframe")).getAsString().split("\\.");
        String reason = Objects.requireNonNull(event.getValue("nod_reason")).getAsString();

        event.reply(translations.ticket_created()).setEphemeral(true).queue();

        assert channel != null;
        channel.sendMessageEmbeds(
                StatsBuilder.buildStatus(Objects.requireNonNull(event.getMember()).getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setTitle(translations.ticket_title().replace("%number%", String.valueOf(ThreadLocalRandom.current().nextInt(1938, 9750))))
                        .setThumbnail(Objects.requireNonNull(event.getMember()).getUser().getAvatarUrl())
                        .setDescription(translations.ticket_body()
                                .replace("%day%", ColorTool.apply(DCColor.GREEN, givenDate[0]))
                                .replace("%month%", ColorTool.apply(DCColor.RED, givenDate[1]))
                                .replace("%year%", ColorTool.apply(DCColor.GOLD, givenDate[2]))
                                .replace("%reason%", reason))
                        .setFooter(translations.ticket_footer()
                                .replace("%date%", date)
                                .replace("%time%", time))
                        .build())
                .addActionRow(
                        Button.success("accept_ticket_notice_of_departure", "Accept Ticket"),
                        Button.danger("dismiss_ticket_notice_of_departure", "Dismiss Ticket")
                ).queue();
    }
}