package dev.vxrp.api.sla.cedmod.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Player(
    @Transient
    var response: Long = 0L,
    val players: List<PlayerObject>,
    val total: Int
)

@Serializable
data class PlayerObject(
    val id: String,
    val userId: String,
    val userName: String,
    val lastSeen: String,
    val firstSeen: String,
    val activity: Double,
    val doNotTrack: Boolean,
    val staff: Boolean,
    val kills: Int,
    val deaths: Int,
    val experiencePoints: Int,
    val colaDrink: Int,
    val medkits: Int,
    val adrenalineShots: Int,
    val survival: Int,
    val pocketEscapes: Int,
    val roundsPlayed: Int,
    val teamkills: Int?,
    val escapeTimes: Int?,
    val level: Int,
    val scpStatsTracked: Boolean,
    val convertedToFormat: Boolean,
    val syncUserId: Boolean,
    val statistics: List<Statistics>?,
    val additionalData: AdditionalData,
    val experiencePointsTotal: Int,
    val expNeededNevelUp: Int
)

@Serializable
data class Statistics(
    @Transient
    val test: String = "test"
)

@Serializable
data class AdditionalData(
    @SerialName("CedModBan")
    val cedModBan: String,
    @SerialName("AltPreventionWhitelisted")
    val altPreventionWhitelisted: String,
    @SerialName("AltPreventionIgnored")
    val atPreventionIgnored: String,
    @SerialName("BanLogs")
    val banLogs: String,
    @SerialName("WarnLogs")
    val warnLogs: String,
    @SerialName("MuteLogs")
    val muteLogs: String,
    @SerialName("Mute")
    val mute: String,
    @SerialName("Ban")
    val ban: String,
    @SerialName("Watchlist")
    val watchlist: String,
    @SerialName("Activity")
    val activity: String,
    @SerialName("GroupWatchlist")
    val groupWatchlist: String
)