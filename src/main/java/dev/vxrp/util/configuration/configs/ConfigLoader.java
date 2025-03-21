package dev.vxrp.util.configuration.configs;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.configuration.ConfigManager;
import dev.vxrp.util.configuration.ConfigurationLoadManager;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.util.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public ConfigLoader(ConfigurationLoadManager configurations) {
        ConfigManager configManager = ScpTools.getConfigManager();

        ConfigGroup configGroup = new ConfigGroup(
                configManager.getString(CONFIG.TOKEN),
                configManager.getString(CONFIG.LOAD_TRANSLATION),
                configManager.getBoolean(CONFIG.DEBUG),
                configManager.getBoolean(CONFIG.ADVANCED_DEBUG),
                configManager.getString(CONFIG.ACTIVITY_TYPE),
                configManager.getString(CONFIG.ACTIVITY_CONTENT),
                configManager.getString(CONFIG.DATABASE.USE_PREDEFINED_DATABASE_SETS),
                configManager.getString(CONFIG.DATABASE.CUSTOM_URL),
                configManager.getString(CONFIG.DATABASE.CUSTOM_TYPE),
                configManager.getString(CONFIG.DATABASE.CUSTOM_USERNAME),
                configManager.getString(CONFIG.DATABASE.CUSTOM_PASSWORD),
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
                configManager.getBoolean(CONFIG.CEDMOD.ACTIVE),
                configManager.getString(CONFIG.CEDMOD.INSTANCE_URL),
                configManager.getString(CONFIG.CEDMOD.API_KEY),
                configManager.getString(CONFIG.CEDMOD.MASTER_BAN_LIST_ID),
                configManager.getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_SUPPORT_TICKETS),
                configManager.getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_UNBAN_TICKETS),
                configManager.getString(CONFIG.SUPPORT_SETTINGS.UNBAN_CHANNEL_ID),
                configManager.getString(CONFIG.NOTICE_OF_DEPARTURE.DESCISION_CHANNEL_ID),
                configManager.getString(CONFIG.NOTICE_OF_DEPARTURE.NOTICE_CHANNEL_ID),
                configManager.getStringList(CONFIG.NOTICE_OF_DEPARTURE.ROLES_ACCESS_NOTICES),
                configManager.getBoolean(CONFIG.NOTICE_OF_DEPARTURE.CHECK_ON_STARTUP),
                configManager.getString(CONFIG.NOTICE_OF_DEPARTURE.CHECK_TYPE),
                configManager.getInt(CONFIG.NOTICE_OF_DEPARTURE.CHECK_RATE),
                configManager.getBoolean(CONFIG.REGULARS.CREATE_EXAMPLE_CONFIGURATION),
                configManager.getBoolean(CONFIG.REGULARS.ONLY_LOAD_CERTAIN_FOLDERS),
                configManager.getStringList(CONFIG.REGULARS.ONLY_LOAD_FOLDERS));

        configurations.addConfigs(configGroup);
    }
}
