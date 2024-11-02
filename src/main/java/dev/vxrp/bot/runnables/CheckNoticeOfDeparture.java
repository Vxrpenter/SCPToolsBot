package dev.vxrp.bot.runnables;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.database.sqlite.SqliteManager;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.builder.StatsBuilder;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.configuration.records.ButtonGroup;
import dev.vxrp.util.configuration.records.NoticeOfDepartureGroup;
import dev.vxrp.util.records.NoticeOfDeparture;
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
    private final static Logger logger = LoggerFactory.getLogger(CheckNoticeOfDeparture.class);
    private static final NoticeOfDepartureGroup translations = LoadedConfigurations.getNoticeOfDepartureMemoryLoad();
    private static final ButtonGroup buttons = LoadedConfigurations.getButtonMemoryLoad();

    public static Runnable runNoticeOfDepartureCheck(JDA api) {
        return () -> {
            logger.info("Checking notice of departures...");
            SqliteManager sqliteManager = ScpTools.getSqliteManager();
            try {
                for (NoticeOfDeparture notice : sqliteManager.getNoticeOfDepartureTableManager().getEveryNoticeOfDeparture()) {
                    String status = ColorTool.apply(DCColor.GREEN, "CLEAN");
                    String[] startTimes = notice.end_time().split("\\.");

                    LocalDate end_date = LocalDate.of(Integer.parseInt(startTimes[2]), Integer.parseInt(startTimes[1]), Integer.parseInt(startTimes[0]));
                    LocalDate now = LocalDate.now();

                    if (now.isEqual(end_date) || now.isAfter(end_date)) {status = ColorTool.apply(DCColor.RED, "INVALID");}
                    logger.info("Checking notice of departure with Id: {} Status: {}", ColorTool.apply(DCColor.GREEN, notice.id()), status);

                    if (now.isEqual(end_date) || now.isAfter(end_date)) {
                        api.awaitReady().retrieveUserById(notice.id()).queue(user -> {
                            logger.info(ColorTool.useCustomColorCodes("&reset&&gold&----------------------- &reset&&red&AUTOMATIC DETECTION UNIT&reset&&gold& ----------------------&reset&"));
                            logger.info("Found invalid notice of departure");
                            MessageChannel channel = api.getTextChannelById(notice.channel_id());

                            assert channel != null;

                            channel.retrieveMessageById(notice.message_id()).onErrorMap(e -> null).queue(message -> {
                                if (message == null) {
                                    logger.info("Found that message of notice does not exist... opting for deletion of database entry");
                                } else {
                                    List<String> pingRoles = LoadedConfigurations.getConfigMemoryLoad().notice_of_departure_roles_access_notices()
                                            .stream()
                                            .map(id -> "<@&" + id + ">")
                                            .collect(Collectors.toList());

                                    message.editMessage(String.join("", pingRoles)).queue();
                                    channel.sendMessage(String.join("", pingRoles)).queue(ping -> ping.delete().queue());
                                    message.editMessageEmbeds(new EmbedBuilder()
                                                    .setDescription(translations.notice_ended_replace()
                                                            .replace("%user%", Objects.requireNonNull(user.getGlobalName()))
                                                            .replace("%date%", now.toString())
                                                            .replace("%actionTaker%", "AUTOMATIC DETECTION UNIT"))
                                                    .build())
                                            .setActionRow(
                                                    Button.danger("delete_notice_of_departure", buttons.delete_notice_of_departure())
                                            ).queue();
                                    logger.info("Updated official notice of departure message in {}", channel.getName());
                                }
                            });

                            Objects.requireNonNull(user).openPrivateChannel().queue(privateChannel -> {
                                privateChannel.sendMessageEmbeds(
                                        StatsBuilder.buildEnded(user.getGlobalName()).build(),
                                        new EmbedBuilder().setDescription(translations.notice_ended()
                                                        .replace("%timeframe%", notice.start_time()+" till "+notice.end_time()))
                                                .build()
                                ).queue();
                                logger.info("Send private message about invalid notice of departure to {}", user.getGlobalName());
                                try {
                                    sqliteManager.getNoticeOfDepartureTableManager().deleteNoticeOfDeparture(notice.id());
                                } catch (SQLException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                logger.info(ColorTool.apply(DCColor.GOLD, "------------------------------------------------------------------------"));
                            });
                        });
                    }
                }
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
