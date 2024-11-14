package dev.vxrp.bot.events.messages;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.LoggingGroup;
import dev.vxrp.util.logger.LoggerManager;
import dev.vxrp.util.records.ticket.Ticket;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class Support {
    private static final LoggingGroup logging = (LoggingGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.LOGGING_GROUP);
    private static final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);

    public static void supportMessageSent(MessageReceivedEvent event) throws SQLException, InterruptedException {
        if (event.getAuthor().isBot()) return;
        if (ScpTools.getSqliteManager().getTicketsTableManager().existsId(event.getMessage().getChannelId())) {
            Ticket ticket = ScpTools.getSqliteManager().getTicketsTableManager().getTicket(event.getMessage().getChannelId());

            new LoggerManager(event.getJDA()).singleMessageLog(Objects.requireNonNull(event.getMember()).getUser(),
                    logging.support_message_logging_action()
                            .replace("%channel%", "<#"+event.getMessage().getChannelId()+">")
                            .replace("%user%", "<@"+event.getAuthor().getId()+">")
                            .replace("%type%", ticket.identifier().toString())
                            .replace("%id%", ticket.id())
                            .replace("%creator%", "<@"+ticket.creatorId()+">")
                            .replace("%handler%", "<@"+ticket.handlerId()+">").replace("<@null>", "None")
                            .replace("%date%", ticket.creation_date()),
                    event.getMessage().getContentRaw(),
                    Objects.requireNonNull(event.getAuthor()).getAvatarUrl(),
                    config.ticket_logging_channel_id(),
                    Color.ORANGE);
        }
    }
}
