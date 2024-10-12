package dev.vxrp.bot;

import dev.vxrp.bot.commands.CommandManager;
import dev.vxrp.bot.commands.help.HelpCommand;
import dev.vxrp.bot.commands.templates.TemplateCommand;
import dev.vxrp.bot.config.managers.ColorTranslationManager;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.config.managers.ConfigManager;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.Enmus.DCColor;
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
    static ColorTranslationManager colorTranslationManager;

    public final static Logger logger = LoggerFactory.getLogger(ScpTools.class);
    public static void main(String[] args) {
        List<String> folders = Arrays.asList("configs", "translations");
        for (String folder : folders) {
            String folderPath = Paths.get(folder).toString();
            new File(folderPath).mkdirs();
            logger.info("Loading configs from {}", folderPath);
        }

        configManager = new ConfigManager();
        translationManager = new TranslationManager();
        colorTranslationManager = new ColorTranslationManager();

        Activity.ActivityType activityType = Activity.ActivityType.valueOf(configManager.getString(CONFIG.ACTIVITY_TYPE));
        logger.info("ActivityType set to {}", ColorTool.apply(DCColor.RED, activityType.toString()));
        String activityContent = configManager.getString(CONFIG.ACTIVITY_CONTENT);
        logger.info("ActivityContent set to {}", ColorTool.apply(DCColor.RED, activityContent));
        if (configManager.getString("guild_id") == null || Objects.equals(configManager.getString("guild_id"), "")) {
            logger.error("Guild id is {}. Process shutting down", ColorTool.apply(DCColor.RED, "null"));
            return;
        } else {
            logger.info("Launching under guild id {}", ColorTool.apply(DCColor.GREEN, configManager.getString("guild_id")));
        }

        JDA api = JDABuilder.createDefault(configManager.getToken(), GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.of(activityType, activityContent))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .build();

        new CommandManager().Initialize(api);
        api.addEventListener(new TemplateCommand(), new HelpCommand());
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static TranslationManager getTranslationManager() {
        return translationManager;
    }
    public static ColorTranslationManager getColorTranslationManager() {
        return colorTranslationManager;
    }
}