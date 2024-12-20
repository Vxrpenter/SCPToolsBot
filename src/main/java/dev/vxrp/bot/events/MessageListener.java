package dev.vxrp.bot.events;

import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import org.slf4j.Logger;
import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.events.messages.Support;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class MessageListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(MessageListener.class);
    private final ConfigGroup config = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!config.do_logging()) return;

        if (ScpTools.getSqliteManager().getTicketsTableManager().existsId(event.getMessage().getChannelId())) {
            try {
                Support.supportMessageSent(event);
            } catch (SQLException | InterruptedException e) {
                logger.error("Could not log messages from ticket channel {} {}", event.getChannel().getId(), e.getMessage());
            }
        }


    }
}
