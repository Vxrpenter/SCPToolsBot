package dev.vxrp.bot.commands.data

import kotlinx.serialization.Serializable

@Serializable
data class CommandList(
    val commands: List<CustomCommand>,
    val statusCommands: List<CustomCommand>,
)

@Serializable
data class CustomCommand(
    val active: Boolean,
    val inherit: String,
    val name: String,
    val description: String,
    val defaultPermissions: List<String>? = null,
    val options: List<Options>? = null,
    val subcommands: List<Subcommands>? = null,
)

@Serializable
data class Subcommands(
    val inherit: String,
    val name: String,
    val description: String,
    val options: List<Options>? = null,
)

@Serializable
data class Options(
    val type: String,
    val name: String,
    val description: String,
    val isRequired: Boolean,
    val choices: List<Choices>? = null
)

@Serializable
data class Choices(
    val name: String,
    val id: String
)