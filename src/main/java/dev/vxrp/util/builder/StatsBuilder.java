package dev.vxrp.util.builder;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.translation.StatusbarGroup;
import net.dv8tion.jda.api.EmbedBuilder;

public class StatsBuilder {
    private static final StatusbarGroup statusbar = (StatusbarGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.STATUSBAR_GROUP);

    public static EmbedBuilder buildStatus(String username) {
        return new EmbedBuilder()
                .setDescription(statusbar.ticket_status()
                        .replace("%status%", ColorTool.useCustomColorCodes("&green&&bold&Open&reset&"))
                        .replace("%creator%", ColorTool.useCustomColorCodes("&gold&&bold&"+username+"&reset&"))
                        .replace("%handler%", ColorTool.useCustomColorCodes("&red&&bold&None&reset&"))
                );
    }
    public static EmbedBuilder buildDismissed(String username) {
        return new EmbedBuilder()
                .setDescription(statusbar.notice_of_departure_dismissed()
                        .replace("%handler%", ColorTool.useCustomColorCodes("&gold&&bold&"+username+"&reset&")));
    }
    public static EmbedBuilder buildAccepted(String username) {
        return new EmbedBuilder()
                .setDescription(statusbar.notice_of_departure_accepted()
                        .replace("%handler%", ColorTool.useCustomColorCodes("&gold&&bold&"+username+"&reset&")));
    }
    public static EmbedBuilder buildEnded(String username) {
        return new EmbedBuilder()
                .setDescription(statusbar.notice_of_departure_ended()
                        .replace("%handler%", ColorTool.useCustomColorCodes("&gold&&bold&"+username+"&reset&")));
    }
    public static EmbedBuilder buildRevoked(String username) {
        return new EmbedBuilder()
                .setDescription(statusbar.notice_of_departure_revoked()
                        .replace("%handler%", ColorTool.useCustomColorCodes("&gold&&bold&"+username+"&reset&")));
    }
}
