package dev.vxrp.bot.util.logger;

import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.event.Level;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class LoggerManager {
    private final JDA api;

    public LoggerManager(JDA api) {
        this.api = api;
    }

    public void singleMessageLog(String action, String message, String thumbnailURL, Level level, String channelID, Color color) throws InterruptedException {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(color)
                .setThumbnail(thumbnailURL)
                .setDescription("""
                        %action%
                        **Logging Level**
                        ```ansi
                        %level%
                        ```
                        **Message**
                        ```ansi
                        %message%
                        ```
                """
                        .replace("%action%", action)
                        .replace("%level%", levelConverter(level))
                        .replace("%message%", message)).build();

        Objects.requireNonNull(api.awaitReady().getTextChannelById(channelID)).sendMessageEmbeds(embed).queue();
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
