package dev.vxrp.bot;

import dev.vxrp.bot.events.ButtonListener;
import dev.vxrp.bot.commands.CommandManager;
import dev.vxrp.bot.commands.help.HelpCommand;
import dev.vxrp.bot.commands.templates.TemplateCommand;
import dev.vxrp.bot.config.managers.ColorConfigManager;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.util.configuration.util.CONFIG;
import dev.vxrp.bot.config.managers.ConfigManager;
import dev.vxrp.bot.events.ModalListener;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.configuration.translations.TranslationLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class ScpTools {
    static ConfigManager configManager;
    static TranslationManager translationManager;
    static ColorConfigManager colorConfigManager;

    public final static Logger logger = LoggerFactory.getLogger(ScpTools.class);
    public static void main(String[] args) {
        initializeConfigs();
        loadConfigs();

        Activity.ActivityType activityType = Activity.ActivityType.valueOf(configManager.getString(CONFIG.ACTIVITY_TYPE));
        logger.info("ActivityType set to {}", ColorTool.apply(DCColor.RED, activityType.toString()));
        String activityContent = configManager.getString(CONFIG.ACTIVITY_CONTENT);
        logger.info("ActivityContent set to {}", ColorTool.apply(DCColor.RED, activityContent));

        checkGuildID();
        initializeBot(activityType, activityContent);
    }

    private static void loadConfigs() {
        try {
            new TranslationLoader();
            logger.info("Loaded translations into memory");
        } catch (Exception e) {
            logger.error("Could not load translation to memory {}", e.getMessage());
        }
    }

    private static void initializeConfigs() {
        List<String> folders = Arrays.asList("configs", "translations");
        for (String folder : folders) {
            String folderPath = Paths.get(folder).toString();
            new File(folderPath).mkdirs();
            logger.info("Loading configs from {}", folderPath);
        }

        try {
            configManager = new ConfigManager();
        } catch (Exception e) {
            logger.error("Could not load configs from configManager : {}", e.getMessage());
        }
        try {
            translationManager = new TranslationManager();
        } catch (Exception e) {
            logger.error("Could not load configs from translationManager : {}", e.getMessage());
        }
        try {
            colorConfigManager = new ColorConfigManager();
        } catch (Exception e) {
            logger.error("Could not load configs from colorConfigManager : {}", e.getMessage());
        }
    }

    private static void checkGuildID() {
        if (configManager.getString("guild_id") == null || Objects.equals(configManager.getString("guild_id"), "")) {
            logger.error("Guild id is {}. Process shutting down", ColorTool.apply(DCColor.RED, "null"));
            return;
        } else {
            logger.info("Launching under guild id {}", ColorTool.apply(DCColor.GREEN, configManager.getString("guild_id")));
        }
    }

    private static void initializeBot(Activity.ActivityType activityType, String activityContent) {
        JDA api = JDABuilder.createDefault(configManager.getToken(), GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.of(activityType, activityContent))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .build();

        new CommandManager().Initialize(api);
        api.addEventListener(new TemplateCommand(), new HelpCommand(),new ButtonListener(), new ModalListener());
        logger.info("Initialized Listeners");
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static TranslationManager getTranslationManager() {
        return translationManager;
    }
    public static ColorConfigManager getColorConfigManager() {
        return colorConfigManager;
    }
}