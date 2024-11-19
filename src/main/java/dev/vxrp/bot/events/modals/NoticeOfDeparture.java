package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.DCColor_DEPRECATED;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.builder.StatsBuilder;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.LoggingGroup;
import dev.vxrp.util.configuration.records.translation.NoticeOfDepartureGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class NoticeOfDeparture {
    private static final ConfigGroup configs = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    private static final NoticeOfDepartureGroup translations = (NoticeOfDepartureGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.NOTICE_OF_DEPARTURE_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);
    private static final LoggingGroup logging = (LoggingGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.LOGGING_GROUP);

    private static final String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    private static final String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

    public static void createNewNoticeOfDeparture(ModalInteractionEvent event) {
        TextChannel channel = Objects.requireNonNull(event.getGuild()).getTextChannelById(configs.notice_of_departure_decision_channel_id());
        String[] givenDate = Objects.requireNonNull(event.getValue("nod_timeframe")).getAsString().split("\\.");
        String reason = Objects.requireNonNull(event.getValue("nod_reason")).getAsString();

        String day = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
        String month = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        event.reply(translations.ticket_created()).setEphemeral(true).queue();

        assert channel != null;
        List<String> pingRoles = configs.notice_of_departure_roles_access_notices()
                .stream()
                .map(id -> "<@&" + id + ">")
                .collect(Collectors.toList());

        channel.sendMessageEmbeds(
                StatsBuilder.buildStatus(Objects.requireNonNull(event.getMember()).getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setTitle(translations.ticket_title().replace("%number%", String.valueOf(ThreadLocalRandom.current().nextInt(1938, 9750))))
                        .setThumbnail(Objects.requireNonNull(event.getMember()).getUser().getAvatarUrl())
                        .setDescription(translations.ticket_body()
                                .replace("%current_day%", ColorTool.apply(DCColor_DEPRECATED.BOLD, day))
                                .replace("%current_month%", ColorTool.apply(DCColor_DEPRECATED.BOLD, month))
                                .replace("%current_year%", ColorTool.apply(DCColor_DEPRECATED.BOLD, year))
                                .replace("%day%", ColorTool.apply(DCColor_DEPRECATED.BOLD, givenDate[0]))
                                .replace("%month%", ColorTool.apply(DCColor_DEPRECATED.BOLD, givenDate[1]))
                                .replace("%year%", ColorTool.apply(DCColor_DEPRECATED.BOLD, givenDate[2]))
                                .replace("%reason%", reason))
                        .setFooter(translations.ticket_footer()
                                .replace("%date%", date)
                                .replace("%time%", time))
                        .build())
                .addActionRow(
                        Button.success("accept_ticket_notice_of_departure"+":"+event.getUser().getId()+":"+ Objects.requireNonNull(event.getValue("nod_timeframe")).getAsString()+":", buttons.accept_notice_of_departure_ticket()),
                        Button.danger("dismiss_ticket_notice_of_departure"+":"+event.getUser().getId()+":"+ Objects.requireNonNull(event.getValue("nod_timeframe")).getAsString()+":", buttons.dismiss_notice_of_departure_ticket())
                ).queue(message -> message.editMessage(String.join("", pingRoles)).queue());
        channel.sendMessage(String.join("", pingRoles)).queue(message -> message.delete().queue());
    }

    public static void acceptNoticeOfDeparture(ModalInteractionEvent event, User user) {
        String messageID = event.getModalId().split(":")[2];
        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();
        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                StatsBuilder.buildAccepted(event.getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setDescription(translations.ticket_accepted()
                                .replace("%timeframe%", event.getModalId().split(":")[4]+" till " +event.getModalId().split(":")[3])
                                .replace("%reason%", reason))
                        .build()
        )).queue();

        Objects.requireNonNull(event.getGuild().getTextChannelById(configs.notice_of_departure_notice_channel_id())).sendMessageEmbeds(
                StatsBuilder.buildStatus(Objects.requireNonNull(event.getMember()).getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setTitle(translations.notice_title()
                                .replace("%user%", Objects.requireNonNull(user.getGlobalName())))
                        .setThumbnail(user.getAvatarUrl())
                        .setDescription(translations.notice_body()
                                .replace("%user%", "<@"+event.getUser().getId()+">")
                                .replace("%timeframe%", event.getModalId().split(":")[4]+" till " +event.getModalId().split(":")[3])
                                .replace("%reason%", Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString()))
                        .setFooter(translations.notice_footer()
                                .replace("%date%", date)
                                .replace("%time%", time))
                        .build())
                .addActionRow(
                        Button.danger("revoke_notice_of_departure"+":"+
                                user.getId()+":"+event.getModalId().split(":")[4]+":"+event.getModalId().split(":")[3]+":", buttons.revoke_notice_of_departure())
                ).queue(message -> {
                    try {
                        ScpTools.getLoggerManager().creationLog(event.getUser(),
                                logging.notice_of_departure_create_logging_action()
                                        .replace("%id%", user.getId())
                                        .replace("%message%", message.getJumpUrl())
                                        .replace("%user%", "<@"+event.getUser().getId()+">")
                                        .replace("%creator%", "<@"+user.getId()+">")
                                        .replace("%expiration%", event.getModalId().split(":")[3])
                                        .replace("%date%", event.getModalId().split(":")[4])
                                        .replace("%reason%", reason),
                                configs.notice_of_departures_logging_channel_id(),
                                Color.GREEN);

                        ScpTools.getSqliteManager().getNoticeOfDepartureTableManager().addNoticeOfDeparture(
                                user.getId(),
                                configs.notice_of_departure_notice_channel_id()+":"+message.getId(),
                                event.getModalId().split(":")[4],
                                event.getModalId().split(":")[3]
                        );
                    } catch (SQLException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
        event.reply(translations.ticket_message_sent()).setEphemeral(true).queue();
    }

    public static void dismissNoticeOfDeparture(ModalInteractionEvent event, User user) throws InterruptedException {
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

        ScpTools.getLoggerManager().creationLog(event.getUser(),
                logging.notice_of_departure_dismiss_logging_action()
                        .replace("%id%", user.getId())
                        .replace("%user%", "<@"+event.getUser().getId()+">")
                        .replace("%creator%", "<@"+user.getId()+">")
                        .replace("%handler%", "<@"+event.getUser().getId()+">")
                        .replace("%expiration%", event.getModalId().split(":")[3])
                        .replace("%date%", event.getModalId().split(":")[4])
                        .replace("%reason%", reason),
                configs.notice_of_departures_logging_channel_id(),
                Color.RED);

        event.reply(translations.ticket_message_sent()).setEphemeral(true).queue();
    }

    public static void revokeNoticeOfDeparture(ModalInteractionEvent event, User user) throws SQLException, InterruptedException {
        ScpTools.getSqliteManager().getNoticeOfDepartureTableManager().deleteNoticeOfDeparture(user.getId());
        Objects.requireNonNull(event.getMessage()).delete().queue();
        event.reply(translations.notice_revoked_message()).setEphemeral(true).queue();
        user.openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessageEmbeds(
                        StatsBuilder.buildRevoked(user.getGlobalName()).build(),
                        new EmbedBuilder()
                                .setDescription(translations.notice_revoked()
                                        .replace("%timeframe%", event.getModalId().split(":")[3]+" till "+event.getModalId().split(":")[2])
                                        .replace("%reason%", Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString()))
                                .build()
                ).queue());
        ScpTools.getLoggerManager().creationLog(event.getUser(),
                logging.notice_of_departure_close_logging_action()
                        .replace("%id%", user.getId())
                        .replace("%user%", "<@"+event.getUser().getId()+">")
                        .replace("%creator%", "<@"+user.getId()+">")
                        .replace("%handler%", "<@"+event.getUser().getId()+">")
                        .replace("%expiration%", event.getModalId().split(":")[2])
                        .replace("%date%", event.getModalId().split(":")[3])
                        .replace("%reason%", Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString()),
                configs.notice_of_departures_logging_channel_id(),
                Color.RED);
    }
}