package dev.vxrp.util

import dev.vxrp.bot.status.data.Instance
import io.github.vxrpenter.secretlab.data.Server

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