package dev.vxrp.scplist.data

import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val accountId: Int,
    val serverId: Int,
    val ip: String,
    val port: Int,
    val online: Boolean,
    val version: String,
    val friendlyFire: Boolean,
    val modded: Boolean,
    val whitelist: Boolean,
    val isoCode: String,
    val players: String,
    val info: String,
    val techList: List<TechList>,
    val pastebin: String,
    val official: Int,
    val distance: Double
)

@Serializable
data class TechList(
    val id: Int,
    val name: String,
    val version: String,
)