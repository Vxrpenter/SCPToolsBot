package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.translation.RegularsGroup;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

public class Regulars {
    private static final Logger logger = LoggerFactory.getLogger(Regulars.class);
    private static final RegularsGroup translations = (RegularsGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.REGULARS_GROUP);

    public static void regularDataModal(ModalInteractionEvent event) throws SQLException, InterruptedException {
        String roleId;
        try {
            roleId = event.getModalId().split(":")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            roleId = null;
        }
        String steamId = Objects.requireNonNull(event.getValue("regular_steamId")).getAsString();
        // steamId for later implementations
        String username = Objects.requireNonNull(event.getValue("regular_username")).getAsString();
        User user = event.getUser();

        ScpTools.getSqliteManager().getRegularsTableManager().addRegular(
                user.getId(), username, roleId, null, 0, null
        );
        event.reply(translations.sync_select_message()).setEphemeral(true).queue();
    }
}
