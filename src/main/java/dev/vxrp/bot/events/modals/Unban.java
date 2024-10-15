package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import dev.vxrp.bot.util.cedmod.CedModApi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.io.IOException;
import java.util.Objects;

public class Unban {
    public static void acceptUnban(ModalInteractionEvent event) throws IOException {
        String messageID = event.getModalId().split(":")[3];

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        User bannedUser = Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getMemberById(event.getModalId().split(":")[1])).getUser();
        String steamID = event.getModalId().split(":")[2];
        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();

        String instanceUrl = ScpTools.getConfigManager().getString(CONFIG.CEDMOD.INSTANCE_URL);
        String apiKey = ScpTools.getConfigManager().getString(CONFIG.CEDMOD.API_KEY);
        String banListID = ScpTools.getConfigManager().getString(CONFIG.CEDMOD.MASTER_BAN_LIST_ID);
        String banID = CedModApi.getBanId(instanceUrl, apiKey, banListID, steamID);

        bannedUser.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                        builderAccepted(bannedUser.getGlobalName()).build(),
                        new EmbedBuilder()
                        .setDescription(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_ACCEPTED)
                                .replace("%steamID%", steamID)
                                .replace("%reason%", reason))
                        .build()
                        )).queue();

        CedModApi.executeUnban(instanceUrl, apiKey, banID, reason);

        event.reply(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)).setEphemeral(true).queue();
    }
    public static void dismissUnban(ModalInteractionEvent event) {
        String messageID = event.getModalId().split(":")[3];

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        User bannedUser = Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getMemberById(event.getModalId().split(":")[1])).getUser();
        String steamID = event.getModalId().split(":")[2];
        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();

        bannedUser.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                        builderDismissed(event.getUser().getGlobalName()).build(),
                        new EmbedBuilder()
                                .setDescription(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_DISMISSED)
                                        .replace("%steamID%", steamID)
                                        .replace("%reason%", reason))
                                .build()
        )).queue();

        event.reply(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)).setEphemeral(true).queue();
    }

    private static EmbedBuilder builderDismissed(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Ticket Answer: [2;31m[1;31mDismissed[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Creator: [2;31m[1;31m[1;33mYou[0m[2;31m[0m | Handler: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
    private static EmbedBuilder builderAccepted(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Ticket Answer: [2;32m[1;32mAccepted[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Creator: [2;31m[1;31m[1;33mYou[0m[2;31m[0m | Handler: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
}
