/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.util

import dev.vxrp.bot.application.data.ApplicationType
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.status.data.Instance
import io.github.vxrpenter.secretlab.data.Server
import net.dv8tion.jda.api.JDA

/**
 * Saves the current upstream version being queried from https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags,
 * for later usage
 */
var upstreamVersion: String? = ""

/**
 * Saves status bots id's in correlation to its respective SCP: Secret Laboratory
 * server port
 */
var statusMappedBots = hashMapOf<String, Int>()

/**
 * Saves server information in correlation to its respective SCP: Secret Laboratory
 * server port
 */
var statusMappedServers = hashMapOf<Int, Server?>()

/**
 * Saves the instance of a status bot in correlation to its respective SCP: Secret Laboratory
 * server port
 */
var statusInstances = hashMapOf<Int, Instance>()

/**
 * Saves the current api sessions status (true: online, false: offline) to allow session and database
 * status comparing
 */
var statusApiSessionStatus: Boolean = true

/**
 * Saves the current server sessions status (true: online, false: offline) in correlation to its respective
 * SCP: Secret Laboratory, to allow session and database status comparing
 */
var statusServerSessionStatus = hashMapOf<Int, Boolean>()

/**
 * Saves the currently selected application types for global editing
 */
var applicationTypeSet: HashSet<ApplicationType> = hashSetOf()

/**
 * Saves the instance of the main discord bot application, for easy access from all classes
 */
var mainApi: JDA? = null

/**
 * Saves the instance of the command manager for easy access from all classes
 */
var mainCommandManager: CommandManager? = null