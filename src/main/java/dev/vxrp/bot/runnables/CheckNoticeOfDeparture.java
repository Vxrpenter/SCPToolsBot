package dev.vxrp.bot.runnables;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.database.sqlite.SqliteManager;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.builder.StatsBuilder;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.NoticeOfDepartureGroup;
import dev.vxrp.bot.util.objects.NoticeOfDeparture;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckNoticeOfDeparture  {
    private final static Logger logger = LoggerFactory.getLogger(ScpTools.class);
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();

    public static Runnable runNoticeOfDepartureCheck(JDA api) {
        return () -> {
            SqliteManager sqliteManager = ScpTools.getSqliteManager();
            try {
                for (NoticeOfDeparture notice : sqliteManager.getEveryNoticeOfDeparture()) {
                    String[] startTimes = notice.end_time().split("\\.");

                    LocalDate end_date = LocalDate.of(Integer.parseInt(startTimes[2]), Integer.parseInt(startTimes[1]), Integer.parseInt(startTimes[0]));
                    LocalDate now = LocalDate.now();

                    if (now.isEqual(end_date) || now.isAfter(end_date)) {
                        api.awaitReady().retrieveUserById(notice.id()).queue(user -> {
                            logger.info(ColorTool.useCustomColorCodes("&reset&&gold&----------------------- &reset&&red&AUTOMATIC DETECTION UNIT&reset&&gold& ----------------------&reset&"));
                            logger.info("Found invalid notice of departure");
                            MessageChannel channel = api.getTextChannelById(notice.channel_id());

                            Objects.requireNonNull(channel).retrieveMessageById(notice.message_id()).queue(message -> {
                                List<String> pingRoles = LoadedConfigurations.getConfigMemoryLoad().notice_of_departure_roles_access_notices()
                                        .stream()
                                        .map(id -> "<@&" + id + ">")
                                        .collect(Collectors.toList());

                                message.editMessage(String.join("", pingRoles)).queue();
                                channel.sendMessage(String.join("", pingRoles)).queue(ping -> ping.delete().queue());
                                message.editMessageEmbeds(new EmbedBuilder()
                                                .setDescription(ColorTool.useCustomColorCodes(translations.notice_ended_replace()
                                                        .replace("%user%", Objects.requireNonNull(user.getGlobalName()))
                                                        .replace("%date%", now.toString())
                                                        .replace("%actionTaker%", "AUTOMATIC DETECTION UNIT")))
                                                .build())
                                        .setActionRow(
                                                Button.danger("delete_notice_of_departure", "Delete Processed Notice of Departure")
                                        ).queue();
                                logger.info("Updated official notice of departure message in {}", channel.getName());
                            });

                            Objects.requireNonNull(user).openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(
                                    StatsBuilder.buildEnded(user.getGlobalName()).build(),
                                    new EmbedBuilder().setDescription(
                                            translations.notice_ended()
                                                    .replace("%timeframe%", notice.start_time()+" till "+notice.end_time()))
                                            .build()
                            ).queue());
                            logger.info("Send private message about invalid notice of departure to {}", user.getGlobalName());
                            try {
                                sqliteManager.deleteNoticeOfDeparture(notice.id());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            logger.info(ColorTool.apply(DCColor.GOLD, "------------------------------------------------------------------------"));
                        });
                    }
                }
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
