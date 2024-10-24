package dev.vxrp.bot.util.configuration.configs;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.ConfigManager;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.ConfigGroup;
import dev.vxrp.bot.util.configuration.util.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public ConfigLoader() {
        ConfigManager configManager = ScpTools.getConfigManager();

        ConfigGroup configGroup = new ConfigGroup(
                configManager.getString(CONFIG.TOKEN),
                configManager.getString(CONFIG.GUILD_ID),
                configManager.getString(CONFIG.ACTIVITY_TYPE),
                configManager.getString(CONFIG.ACTIVITY_CONTENT),
                configManager.getString(CONFIG.RULES.PASTEBIN),
                configManager.getString(CONFIG.RULES.EMBED_FOOTER),
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

        LoadedConfigurations.setConfigMemoryLoad(configGroup);
        logger.info("Loaded config into memory");
    }
}
