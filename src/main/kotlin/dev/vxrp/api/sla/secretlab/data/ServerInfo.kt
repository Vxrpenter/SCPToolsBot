package dev.vxrp.api.sla.secretlab.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@Serializable
data class ServerInfo(
    @Transient
    var response: Long = 0L,
    @SerialName("Success")
    val success: Boolean = false,
    @SerialName("Cooldown")
    val cooldown: Int? = null,
    @SerialName("Servers")
    val servers: List<Server>,
)

@Serializable
data class Server(
    @SerialName("ID")
    val id: Int,
    @SerialName("Port")
    val port: Int,
    @SerialName("Online")
    val online: Boolean,
    @SerialName("LastOnline")
    val lastOnline: String,
    @SerialName("Players")
    val players: String? = null,
    @SerialName("PlayersList")
    val playerList: List<PlayerList>? = null,
    @SerialName("Info")
    val info: String? = null,
    @SerialName("Version")
    val version: String? = null,
    @SerialName("Pastebin")
    val pasteBin: String? = null,
    @SerialName("FF")
    val ff: Boolean? = null,
    @SerialName("WL")
    val wl: Boolean? = null,
    @SerialName("Modded")
    val modded: Boolean? = null,
    @SerialName("Mods")
    val mods: Int? = null,
    @SerialName("Suppress")
    val support: Int? = null,
    @SerialName("AutoSuppress")
    val autoSuppress: Boolean? = null
)

@Serializable
data class PlayerList(
    @SerialName("ID")
    val id: String? = null,
    @SerialName("Nickname")
    val nickname: String? = null
)