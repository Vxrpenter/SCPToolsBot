package dev.vxrp.bot;

import dev.vxrp.bot.config.ConfigManager;
import dev.vxrp.bot.util.PermissionListConverter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.List;

public class ScpTools {
    public static void main(String[] args) {
        ConfigManager configManager = new ConfigManager();
        Activity.ActivityType activityType = Activity.ActivityType.valueOf(configManager.getString("activity_type"));
        String activityContent = configManager.getString("activity_content");

        JDA api = JDABuilder.createDefault(configManager.getToken(), GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.of(activityType, activityContent))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .build();

        List<String> helpDefaultPermissions = configManager.getStringList("command_settings.help.default_permission");
        String helpDescription = configManager.getString("command_settings.help.description");

        api.updateCommands().addCommands(
                Commands.slash("help", helpDescription)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(new PermissionListConverter(helpDefaultPermissions).convert()))
        ).queue();
    }
}
