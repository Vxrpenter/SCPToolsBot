package dev.vxrp.util.configuration.configs;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.configuration.ConfigManager;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.configuration.records.ConfigGroup;
import dev.vxrp.util.configuration.util.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public ConfigLoader() {
        ConfigManager configManager = ScpTools.getConfigManager();

        ConfigGroup configGroup = new ConfigGroup(
                configManager.getString(CONFIG.TOKEN),
                configManager.getBoolean(CONFIG.DEBUG),
                configManager.getBoolean(CONFIG.ADVANCED_DEBUG),
                configManager.getString(CONFIG.ACTIVITY_TYPE),
                configManager.getString(CONFIG.ACTIVITY_CONTENT),
                configManager.getString(CONFIG.RULES.PASTEBIN),
                configManager.getString(CONFIG.RULES.EMBED_FOOTER),
                configManager.getBoolean(CONFIG.LOGGING.DO_LOGGING),
                configManager.getString(CONFIG.LOGGING.TICKET_CHANNEL_ID),
                configManager.getString(CONFIG.LOGGING.TICKET_BACKUP_CHANNEL_ID),
                configManager.getString(CONFIG.LOGGING.NOTICE_OF_DEPARTURE_CHANNEL_ID),
                configManager.getBoolean(CONFIG.LOGGING.DO_DATABASE_LOGGING),
                configManager.getString(CONFIG.LOGGING.DATABASE_CHANNEL_ID),
                configManager.getStringList(CONFIG.COMMANDS),
                configManager.getStringList(CONFIG.COMMAND_SETTINGS.DEFAULT_PERMISSIONS.HELP),
                configManager.getString(CONFIG.COMMAND_SETTINGS.DESCRIPTIONS.HELP),
                configManager.getStringList(CONFIG.COMMAND_SETTINGS.DEFAULT_PERMISSIONS.TEMPLATE),
                configManager.getString(CONFIG.COMMAND_SETTINGS.DESCRIPTIONS.TEMPLATE),
                configManager.getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_SUPPORT_TICKETS),
                configManager.getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_UNBAN_TICKETS),
                configManager.getString(CONFIG.SUPPORT_SETTINGS.UNBAN_CHANNEL_ID),
                configManager.getString(CONFIG.NOTICE_OF_DEPARTURE.DESCISION_CHANNEL_ID),
                configManager.getString(CONFIG.NOTICE_OF_DEPARTURE.NOTICE_CHANNEL_ID),
                configManager.getStringList(CONFIG.NOTICE_OF_DEPARTURE.ROLES_ACCESS_NOTICES),
                configManager.getBoolean(CONFIG.NOTICE_OF_DEPARTURE.CHECK_ON_STARTUP),
                configManager.getString(CONFIG.NOTICE_OF_DEPARTURE.CHECK_TYPE),
                configManager.getInt(CONFIG.NOTICE_OF_DEPARTURE.CHECK_RATE),
                configManager.getBoolean(CONFIG.CEDMOD.ACTIVE),
                configManager.getString(CONFIG.CEDMOD.INSTANCE_URL),
                configManager.getString(CONFIG.CEDMOD.API_KEY),
                configManager.getString(CONFIG.CEDMOD.MASTER_BAN_LIST_ID));

        logger.warn("Loading configurations, this could take some time...");
        for (var component : configGroup.getClass().getRecordComponents()) {
            try {
                logger.trace("Added value to button translations - {}", component.getAccessor().invoke(configGroup));
            } catch (Exception e) {debuggerErrorHandler(e);}
        }
        LoadedConfigurations.setConfigMemoryLoad(configGroup);
        logger.info("Loaded configurations");
    }

    private static void debuggerErrorHandler(Exception e) {
        logger.debug("{} Could not log the exact configuration value (this error can be ignored) - Stacktrace {}", ColorTool.apply(DCColor.RED, "ERROR") ,e.getMessage());
    }
}
