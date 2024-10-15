package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.util.cedmod.CedModApi;
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

        CedModApi.executeUnban(instanceUrl, apiKey, banID, reason);
    }
    public static void dismissUnban(ModalInteractionEvent event) {

    }
}
