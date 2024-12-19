package dev.vxrp.api.sla.scplist.data

import dev.vxrp.api.sla.scplist.data.Server
import kotlinx.serialization.Serializable

@Serializable
data class ScpListServers(
    val onlineUserCount: Int,
    val onlineServerCount: Int,
    val displayUserCount: Int,
    val displayServerCount: Int,
    val offlineServerCount: Int,
    val servers: List<Server>? = null)

