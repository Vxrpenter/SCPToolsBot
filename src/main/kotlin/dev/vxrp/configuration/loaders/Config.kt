package dev.vxrp.configuration.loaders

import dev.vxrp.bot.status.data.Status
import dev.vxrp.bot.ticket.data.Ticket
import dev.vxrp.util.launch.data.LaunchConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Config(
    val launchConfiguration: LaunchConfiguration,
    val settings: Settings,
    val status: Status,
    val ticket: Ticket
)

@Serializable
data class Settings(
    val token: String,
    @SerialName("client_secret")
    val clientSecret: String,
    @SerialName("guild_id")
    val guildId: String,
    @SerialName("load_translation")
    val loadTranslation: String,
    val debug: Boolean,
    @SerialName("advanced_debug")
    val advancedDebug: Boolean,
    @SerialName("activity_type")
    val activityType: String,
    @SerialName("activity_content")
    val activityContent: String,
    val database: ConfigDatabase,
    val cedmod: ConfigCedmod,
    @SerialName("notice_of_departure")
    val noticeOfDeparture: ConfigNoticeOfDeparture,
    val regulars: ConfigRegulars
)

@Serializable
data class ConfigDatabase(
    @SerialName("use_predefined_database_sets")
    val dataUsePredefined: String,
    @SerialName("custom_type")
    val customType: String,
    @SerialName("custom_url")
    val customUrl: String,
    @SerialName("custom_username")
    val customUsername: String,
    @SerialName("custom_password")
    val customPassword: String
)

@Serializable
data class ConfigCedmod(
    @SerialName("active")
    val active: Boolean,
    @SerialName("instance_url")
    val instance: String,
    @SerialName("api_key")
    val api: String,
)

@Serializable
data class ConfigNoticeOfDeparture(
    @SerialName("decision_channel_id")
    val decisionChannel: String,
    @SerialName("notice_channel_id")
    val noticeChannel: String,
    @SerialName("roles_access_notices")
    val rolesAccess: List<String>,
    @SerialName("check_type")
    val checkUnit: String,
    @SerialName("check_rate")
    val checkRate: Int
)

@Serializable
data class ConfigRegulars(
    @SerialName("create_example_configuration")
    val createExample: Boolean,
    @SerialName("only_load_certain_folder")
    val onlyLoad: Boolean,
    @SerialName("only_load_folders")
    val loadFolders: List<String>
)