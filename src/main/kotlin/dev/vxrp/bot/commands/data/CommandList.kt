/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

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