package dev.vxrp.bot;

import dev.vxrp.bot.commands.CommandManager;
import dev.vxrp.bot.commands.help.HelpCommand;
import dev.vxrp.bot.commands.help.InfoCommand;
import dev.vxrp.bot.commands.templates.TemplateCommand;
import dev.vxrp.bot.config.CONFIG;
import dev.vxrp.bot.config.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class ScpTools {
    static ConfigManager configManager;
    public static void main(String[] args) {
        configManager = new ConfigManager();
        Activity.ActivityType activityType = Activity.ActivityType.valueOf(configManager.getString(CONFIG.ACTIVITY_TYPE));
        String activityContent = configManager.getString(CONFIG.ACTIVITY_CONTENT);

        JDA api = JDABuilder.createDefault(configManager.getToken(), GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.of(activityType, activityContent))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .build();

        new CommandManager().Initialize(api);
        api.addEventListener(new TemplateCommand(), new HelpCommand(), new InfoCommand());
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
