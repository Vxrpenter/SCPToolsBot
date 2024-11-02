package dev.vxrp.util.logger;

import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class LoggerManager {
    private final String filler = "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E";
    private final static Logger logger = LoggerFactory.getLogger(LoggerManager.class);
    private final JDA api;

    public LoggerManager(JDA api) {
        this.api = api;
    }

    public void singleMessageLog(User user, String action, String message, String thumbnailURL, String channelID, Color color) throws InterruptedException {
        try {
            MessageEmbed info = new EmbedBuilder()
                    .setColor(color)
                    .setAuthor(user.getName(), null, thumbnailURL)
                    .setDescription(LoadedConfigurations.getLoggingMemoryLoad().single_message_log_template()
                            .replace("%action%", action.replace("%filler%", filler))
                            .replace("%message%", message.strip().replace("```", "<@CODEBLOCK>"))).build();

            Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info).queue();
        } catch (IllegalArgumentException e) {
            logger.warn("Could not correctly print out message log because of too many characters... opting for empty embed");
            MessageEmbed info = new EmbedBuilder()
                    .setColor(color)
                    .setAuthor(user.getName(), null, thumbnailURL)
                    .setDescription(LoadedConfigurations.getLoggingMemoryLoad().single_message_log_template()
                            .replace("%action%", action.replace("%filler%", filler))
                            .replace("%message%", ColorTool.useCustomColorCodes("&red&&bold&Too long for Embed...&reset&"))).build();
            Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info, new EmbedBuilder().setColor(color).setDescription(message).build()).queue();
        }
    }

    public void multiMessageLog(List<Message> messages, String action, String channelID) {
        messages.forEach(message -> {
            try {
                singleMessageLog(message.getAuthor(), action, message.getContentRaw(), message.getAuthor().getAvatarUrl(), channelID, Color.CYAN);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void creationLog(User user, String action, String channelID, Color color) throws InterruptedException {
        MessageEmbed info = new EmbedBuilder()
                .setColor(color)
                .setAuthor(user.getGlobalName(), null, user.getAvatarUrl())
                .setDescription(LoadedConfigurations.getLoggingMemoryLoad().close_log_template()
                        .replace("%action%", action.replace("%filler%", filler))).build();

        Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info).queue();
    }

    public void closeLog(User user, String action, String channelID, Color color) throws InterruptedException {
        MessageEmbed info = new EmbedBuilder()
                .setColor(color)
                .setAuthor(user.getGlobalName(), null, user.getAvatarUrl())
                .setDescription(LoadedConfigurations.getLoggingMemoryLoad().close_log_template()
                        .replace("%action%", action.replace("%filler%", filler))).build();

        Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info).queue();
    }

    public void databaseLog(String sqlStatement, String output, String channelID, Color color) throws InterruptedException {
        if (!LoadedConfigurations.getConfigMemoryLoad().do_database_logging()) return;
        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        MessageEmbed info = new EmbedBuilder()
                .setColor(color)
                .setAuthor("Database Manager",
                        "https://github.com/Vxrpenter/SCPToolsBot/blob/master/src/main/java/dev/vxrp/bot/database/sqlite/SqliteManager.java",
                        "https://toppng.com/uploads/preview/database-database-icon-11563207079binxarjjyp.png")
                .setDescription(LoadedConfigurations.getLoggingMemoryLoad().database_log_template()
                        .replace("%statement%", sqlStatement)
                        .replace("%output%", output))
                .setFooter("Statement sent on the "+date+" at "+time+"h")
                .build();

        Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info).queue();
    }

    private String levelConverter(Level level) {
        switch (level) {
            case TRACE -> {
                return ColorTool.apply(DCColor.DARK_GRAY, "TRACE");
            }
            case DEBUG -> {
                return ColorTool.apply(DCColor.GOLD, "DEBUG");
            }
            case INFO -> {
                return ColorTool.apply(DCColor.LIGHT_BLUE, "INFO");
            }
            case WARN -> {
                return ColorTool.apply(DCColor.PINK, "WARN");
            }
            case ERROR -> {
                return ColorTool.apply(DCColor.RED, "ERROR");
            }
        }
        return ColorTool.apply(DCColor.TEAL, level.toString());
    }
}
