package dev.vxrp.bot.commands;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.converter.PermissionListConverter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.Command;
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
    private final static Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private final static ConfigGroup configs = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);

    final JDA api;
    final List<CommandData> commands;

    public CommandManager(JDA api) {
        this.api = api;
        this.commands = new ArrayList<>();
    }

    public CommandManager addCommand(String commandName, String description, List<Permission> permissions , OptionData options) {
        if (options != null) {
            commands.add(Commands.slash(commandName, description)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions))
                    .addOptions(options));
        } else {
            commands.add(Commands.slash(commandName, description)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions)));
        }

        return this;
    }

    public void build() {
        List<String> activeCommands = configs.commands();

        for (CommandData command : commands) {
            if (activeCommands.contains(command.getName())) continue;
            api.updateCommands().addCommands(command).queue();
            logger.info("Initialized command: {}", command.getName());
        }
        api.addEventListener(new CommandListener());
    }
}