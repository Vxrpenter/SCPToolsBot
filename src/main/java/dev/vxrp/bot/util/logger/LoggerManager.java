package dev.vxrp.bot.util.logger;

import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class LoggerManager {
    private final String filler = "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E";
    private final static Logger logger = LoggerFactory.getLogger(LoggerManager.class);
    private final JDA api;

    public LoggerManager(JDA api) {
        this.api = api;
    }

    public void singleMessageLog(User user, String action, String message, String thumbnailURL, Level level, String channelID, Color color) throws InterruptedException {
        try {
            MessageEmbed info = new EmbedBuilder()
                    .setColor(color)
                    .setAuthor(user.getName(), null, thumbnailURL)
                    .setDescription(LoadedConfigurations.getLoggingMemoryLoad().single_message_log_template()
                            .replace("%action%", action.replace("%filler%", filler))
                            .replace("%level%", levelConverter(level))
                            .replace("%message%", message.strip())).build();

            Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info).queue();
        } catch (IllegalArgumentException e) {
            MessageEmbed info = new EmbedBuilder()
                    .setColor(color)
                    .setAuthor(user.getName(), null, thumbnailURL)
                    .setDescription(LoadedConfigurations.getLoggingMemoryLoad().single_message_log_template()
                            .replace("%action%", action.replace("%filler%", filler))
                            .replace("%level%", levelConverter(level))
                            .replace("%message%", "")).build();
            logger.warn("Could not correctly print out message log because of too many characters opting for empty embed");
            Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(info, new EmbedBuilder().setColor(color).setDescription(message).build()).queue();
        }
    }

    public void multiMessageLog(String[] messages, URL ThumbnailURL, Level level, String channelID) {

    }

    public void creationLog() {

    }

    public void deletionLog() {

    }

    public void databaseLog() {

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
