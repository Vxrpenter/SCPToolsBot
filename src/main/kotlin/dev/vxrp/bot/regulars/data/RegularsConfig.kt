package dev.vxrp.bot.regulars.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegularsConfig(
    val roles: List<RegularsConfigRole>
)

@Serializable
data class  RegularsConfigRole(
    val id: String,
    val description: String,
    @SerialName("requirement_type")
    val requirementType: String,
    @SerialName("playtime_requirement")
    val playtimeRequirements: Int,
    @SerialName("xp_requirement")
    val xpRequirements: Int
)
