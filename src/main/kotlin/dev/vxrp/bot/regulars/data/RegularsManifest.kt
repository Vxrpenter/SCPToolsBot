package dev.vxrp.bot.regulars.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegularsManifest(
    val name: String,
    val description: String,
    @SerialName("custom_role")
    val customRole: RegularsManifestCustomRole
)

@Serializable
data class RegularsManifestCustomRole(
    val use: Boolean,
    val id: String
)
