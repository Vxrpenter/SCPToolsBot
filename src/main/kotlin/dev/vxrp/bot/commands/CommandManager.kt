package dev.vxrp.bot.commands

import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.managers.ConfigManager
import dev.vxrp.bot.commands.data.CommandList
import dev.vxrp.bot.commands.data.CustomCommand
import dev.vxrp.bot.commands.data.Options
import dev.vxrp.bot.commands.data.Subcommands
import kotlinx.serialization.json.Json
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
import java.io.File

class CommandManager(val config: Config, val file: String) {
    private val logger = LoggerFactory.getLogger(CommandManager::class.java)
    private val currentFile = File(System.getProperty("user.dir")).resolve(file)

    init {
        if (!currentFile.exists()) {
            currentFile.createNewFile()

            val content = ConfigManager::class.java.getResourceAsStream("/$file")
            if (content != null) currentFile.appendBytes(content.readBytes())
        }
    }

    fun registerSpecificCommands(commands: List<CustomCommand>, api: JDA) {
        val commandList = mutableListOf<CommandData>()
        for (command in commands) {
            if (!command.active) continue

            val permissions = mutableListOf<Permission>()
            if (command.defaultPermissions != null) {
                command.defaultPermissions.forEach { permission -> permissions.add(Permission.valueOf(permission)) }
            }

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

            if (option.choices != null) {
                option.choices.size.let { it -> repeat(it) { choices.add(Command.Choice(option.choices[it].name, option.choices[it].id))} }
            }
            optionData.add(OptionData(OptionType.valueOf(option.type), option.name, option.description, option.isRequired).also { if (choices.isNotEmpty()) { it.addChoices(choices) }})
        }

        return optionData
    }

    private fun addSubcommands(subcommands: List<Subcommands>?): List<SubcommandData> {
        val subcommandData = mutableListOf<SubcommandData>()
        for (subcommand in subcommands!!) {
            val options = mutableListOf<OptionData>()

            val currentSubCommand = SubcommandData(subcommand.name, subcommand.description).also { commandData ->
                if (subcommand.options != null) {
                    commandData.addOptions(addOptions(subcommand.options))
                }
            }

            subcommandData.add(currentSubCommand)
        }
        return subcommandData
    }

    fun query(): CommandList {
        return Json.decodeFromString<CommandList>(currentFile.readText())
    }
}


