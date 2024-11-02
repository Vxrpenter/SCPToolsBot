package dev.vxrp.bot.commands;

import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.configuration.records.ConfigGroup;
import dev.vxrp.util.converter.PermissionListConverter;
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
    private final static Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private final static ConfigGroup configs = LoadedConfigurations.getConfigMemoryLoad();

    public void Initialize(JDA api) {
        List<String> activeCommands = configs.commands();
        List<String> helpDefaultPermissions = configs.command_setting_help_default_permissions();
        List<String> templateDefaultPermissions = configs.command_settings_template_default_permissions();

        List<CommandData> commands = new ArrayList<>();
        if (activeCommands.contains("help")) {
            commands.add(Commands.slash("help", configs.command_settings_help_description())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(new PermissionListConverter(helpDefaultPermissions).convert())));
        } else {
            logger.warn("Command help is deactivated, it is highly recommended to leave this command on");
        }
        if (activeCommands.contains("template")) {
            commands.add(Commands.slash("template", configs.command_settings_template_descriptions())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(new PermissionListConverter(templateDefaultPermissions).convert()))
                    .addOptions(
                            new OptionData(OptionType.STRING, "template", "What template are you referring to", true)
                                    .addChoice("rules", "rules")
                                    .addChoice("support", "support")
                                    .addChoice("notice of departure", "notice_of_departure")
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