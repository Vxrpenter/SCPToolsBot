package dev.vxrp.bot.commands.data

import dev.vxrp.bot.status.data.Instance
import dev.vxrp.secretlab.data.Server

class StatusConstructor(
    bots: HashMap<String, Int>, servers: HashMap<Int, Server>, var instance: Instance) {
    var mappedBots: HashMap<String, Int> = bots

    var mappedServers: HashMap<Int, Server> = servers
}