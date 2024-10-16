package dev.vxrp.bot.commands;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.util.PermissionListConverter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public final static Logger logger = LoggerFactory.getLogger(CommandManager.class);
    public CommandManager() {}
    public void Initialize(JDA api) {
        List<String> activeCommands = ScpTools.getConfigManager().getStringList(CONFIG.COMMANDS);
        List<String> helpDefaultPermissions = ScpTools.getConfigManager().getStringList(CONFIG.COMMAND_SETTINGS.DEFAULT_PERMISSIONS.HELP);
        List<String> templateDefaultPermissions = ScpTools.getConfigManager().getStringList(CONFIG.COMMAND_SETTINGS.DEFAULT_PERMISSIONS.TEMPLATE);

        List<CommandData> commands = new ArrayList<>();
        if (activeCommands.contains("help")) {
            commands.add(Commands.slash("help", ScpTools.getConfigManager().getString(CONFIG.COMMAND_SETTINGS.DESCRIPTIONS.HELP))
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(new PermissionListConverter(helpDefaultPermissions).convert())));
        } else {
            logger.warn("Command help is deactivated, it is highly recommended to leave this command on");
        }
        if (activeCommands.contains("template")) {
            commands.add(Commands.slash("template", ScpTools.getConfigManager().getString(CONFIG.COMMAND_SETTINGS.DESCRIPTIONS.TEMPLATE))
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(new PermissionListConverter(templateDefaultPermissions).convert()))
                    .addOptions(
                            new OptionData(OptionType.STRING, "template", "What template are you referring to", true)
                                    .addChoice("rules", "rules")
                                    .addChoice("support", "support")
                                    .addChoice("deregistration", "deregistration")
                                    .addChoice("regulars", "regulars")
                                    .addChoice("adminpanel", "adminpanel")
                    ));
        }
        for (String command : activeCommands) {
            api.updateCommands().addCommands(commands).queue();
            logger.info("Initialized command: {}", command);
        }
    }
}