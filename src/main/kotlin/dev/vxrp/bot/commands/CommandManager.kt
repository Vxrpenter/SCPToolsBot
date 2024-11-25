package dev.vxrp.bot.commands

import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.managers.ConfigManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.slf4j.LoggerFactory
import java.io.File

@Serializable
data class CommandList(val commands: List<CustomCommand>)

@Serializable
data class CustomCommand(val active: Boolean,
                         val inherit: String,
                         val name: String,
                         val description: String,
                         @SerialName("default_permissions")
                         val defaultPermissions: List<String>,
                         val options: List<Options>)

@Serializable
data class Options(val type: String, val name: String, val description: String, val isRequired: Boolean, val choices: List<Choices>)

@Serializable
data class Choices(val name: String, val id: String)

class CommandManager(val api: JDA, val config: Config, val file: String) {
    private val logger = LoggerFactory.getLogger(CommandManager::class.java)
    private val currentFile = File("${System.getProperty("user.dir")}$file")

    init {
        if (!currentFile.exists()) {
            currentFile.createNewFile()

            val content = ConfigManager::class.java.getResourceAsStream(file)
            if (content != null) currentFile.appendBytes(content.readBytes())
        }
    }

    fun registerCommands() {
        val data = query()

        for (command in data.commands) {
            if (!command.active) continue

            val permissions = mutableListOf<Permission>()
            command.defaultPermissions.forEach { permission -> permissions.add(Permission.valueOf(permission)) }

            val currentCommand = Commands.slash(command.name, command.description).also { commandData ->
                if (command.options.isNotEmpty()) commandData.addOptions(addOptions(command))

            }.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions))

            api.updateCommands().addCommands(currentCommand)
            logger.info("Registering command ${command.name}")
        }
    }

    private fun addOptions(command: CustomCommand): List<OptionData> {
        val optionData = mutableListOf<OptionData>()
        for (option in command.options) {
            val choices = mutableListOf<Command.Choice>()

            if (option.choices.isNotEmpty()) { repeat(option.choices.size) { choices.add(Command.Choice(option.name, option.description))} }
            optionData.add(OptionData(OptionType.valueOf(option.type), option.name, option.description, option.isRequired).also { if (choices.isNotEmpty()) { it.addChoices(choices) } })
        }

        return optionData
    }

    fun query(): CommandList {
        return Json.decodeFromString<CommandList>(currentFile.readText())
    }
}


