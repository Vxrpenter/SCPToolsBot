package dev.vxrp.bot.commands

import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.vxrp.bot.commands.data.CustomCommand
import dev.vxrp.bot.commands.data.Options
import dev.vxrp.bot.commands.data.Subcommands
import dev.vxrp.configuration.data.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.slf4j.LoggerFactory

class CommandManager(val config: Config) {
    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    fun registerSpecificCommands(commands: List<CustomCommand>, api: JDA) {
        val commandList = mutableListOf<CommandData>()
        for (command in commands) {
            if (!command.active) continue

            val permissions = mutableListOf<Permission>()
            command.defaultPermissions?.forEach { permission -> permissions.add(Permission.valueOf(permission)) }

            val currentCommand = Commands.slash(command.name, command.description).also { commandData ->
                if (command.options != null) {
                    commandData.addOptions(addOptions(command.options))
                }
                if (command.subcommands != null) {
                    commandData.addSubcommands(addSubcommands(command.subcommands))
                }
            }.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions))

            commandList.add(currentCommand)
            logger.info("Registering command ${command.name} for bot: ${api.selfUser.name} (${api.selfUser.id})")
        }

        api.updateCommands {
            addCommands(commandList)
        }.queue()
    }

    private fun addOptions(options: List<Options>?): List<OptionData> {
        val optionData = mutableListOf<OptionData>()
        for (option in options!!) {
            val choices = mutableListOf<Command.Choice>()

            option.choices?.size?.let { it -> repeat(it) { choices.add(Command.Choice(option.choices[it].name, option.choices[it].id)) } }
            optionData.add(OptionData(OptionType.valueOf(option.type), option.name, option.description, option.isRequired).also {
                    if (choices.isNotEmpty()) {
                        it.addChoices(choices)
                    }
            })
        }

        return optionData
    }

    private fun addSubcommands(subcommands: List<Subcommands>?): List<SubcommandData> {
        val subcommandData = mutableListOf<SubcommandData>()
        for (subcommand in subcommands!!) {

            val currentSubCommand = SubcommandData(subcommand.name, subcommand.description).also { commandData ->
                if (subcommand.options != null) {
                    commandData.addOptions(addOptions(subcommand.options))
                }
            }

            subcommandData.add(currentSubCommand)
        }
        return subcommandData
    }
}


