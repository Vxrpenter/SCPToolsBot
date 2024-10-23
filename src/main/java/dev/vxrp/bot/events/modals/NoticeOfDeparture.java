package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.builder.StatsBuilder;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.ConfigGroup;
import dev.vxrp.bot.util.configuration.groups.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
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

        String day = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
        String month = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        event.reply(translations.ticket_created()).setEphemeral(true).queue();

        assert channel != null;
        channel.sendMessageEmbeds(
                StatsBuilder.buildStatus(Objects.requireNonNull(event.getMember()).getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setTitle(translations.ticket_title().replace("%number%", String.valueOf(ThreadLocalRandom.current().nextInt(1938, 9750))))
                        .setThumbnail(Objects.requireNonNull(event.getMember()).getUser().getAvatarUrl())
                        .setDescription(translations.ticket_body()
                                .replace("%current_day%", ColorTool.apply(DCColor.BOLD, day))
                                .replace("%current_month%", ColorTool.apply(DCColor.BOLD, month))
                                .replace("%current_year%", ColorTool.apply(DCColor.BOLD, year))
                                .replace("%day%", ColorTool.apply(DCColor.BOLD, givenDate[0]))
                                .replace("%month%", ColorTool.apply(DCColor.BOLD, givenDate[1]))
                                .replace("%year%", ColorTool.apply(DCColor.BOLD, givenDate[2]))
                                .replace("%reason%", reason))
                        .setFooter(translations.ticket_footer()
                                .replace("%date%", date)
                                .replace("%time%", time))
                        .build())
                .addActionRow(
                        Button.success("accept_ticket_notice_of_departure", "Accept Ticket"),
                        Button.danger("dismiss_ticket_notice_of_departure"+":"+event.getUser().getId()+":", "Dismiss Ticket")
                ).queue();
    }

    public static void dismissUnban(ModalInteractionEvent event, User user) {
        String messageID = event.getModalId().split(":")[2];
        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();
        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                StatsBuilder.buildDismissed(event.getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setDescription(translations.ticket_dismissed()
                                .replace("%reason%", reason))
                        .build()
        )).queue();

        event.reply(translations.ticket_message_sent()).setEphemeral(true).queue();
    }
}