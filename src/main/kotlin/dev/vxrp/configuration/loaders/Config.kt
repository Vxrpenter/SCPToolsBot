package dev.vxrp.configuration.loaders

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(val token: String,
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
                  //database
                  val database: ConfigDatabase,
                  //rules
                  val rules: ConfigRules,
                  //logging
                  val logging: ConfigLogging,
                  @SerialName("commands")
                  val commands: List<String>,
                  //command-settings
                  @SerialName("command_settings")
                  val commandSettings: ConfigCommandSettings,
                  //cedmod
                  val cedmod: ConfigCedmod,
                  //support-settings
                  val support: ConfigSupport,
                  //notice-of-departure
                  @SerialName("notice_of_departure")
                  val noticeOfDeparture: ConfigNoticeOfDeparture,
                  //regulars
                  val regulars: ConfigRegulars)

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
    val customPassword: String)

@Serializable
data class ConfigRules(
    @SerialName("pastebin")
    val pastbin: String,
    @SerialName("embed_footer")
    val footer: String)

@Serializable
data class ConfigLogging(
    @SerialName("do_logging")
    val active: Boolean,
    @SerialName("ticket_logging_channel_id")
    val ticketChannel: String,
    @SerialName("notice_of_departures_logging_channel_id")
    val noticeOfDepartureChannel: String,
    @SerialName("do_database_logging")
    val databaseLog: Boolean,
    @SerialName("database_logging_channel_id")
    val databaseChannel: String)

@Serializable
data class ConfigCommandSettings(
    val help: ConfigHelpCommand,
    val template: ConfigTemplateCommand,
    @SerialName("notice_of_departure")
    val noticeOfDeparture: ConfigNoticeOfDepartureCommand,
    val regulars: ConfigRegularsCommand,)

@Serializable
data class ConfigHelpCommand(
    @SerialName("default_permissions")
    val defaultPermission: List<String>,
    val description: String)

@Serializable
data class ConfigTemplateCommand(
    @SerialName("default_permissions")
    val defaultPermission: List<String>,
    val description: String)

@Serializable
data class ConfigNoticeOfDepartureCommand(
    @SerialName("default_permissions")
    val defaultPermission: List<String>,
    val description: String)

@Serializable
data class ConfigRegularsCommand(
    @SerialName("default_permissions")
    val defaultPermission: List<String>,
    val description: String)

@Serializable
data class ConfigCedmod(
    @SerialName("active")
    val active: Boolean,
    @SerialName("instance_url")
    val instance: String,
    @SerialName("api_key")
    val api: String,
    @SerialName("master_ban_list_id")
    val mastBanList: String, )

@Serializable
data class ConfigSupport(
    @SerialName("roles_access_support_tickets")
    val rolesAccessSupport: List<String>,
    @SerialName("roles_access_unban_tickets")
    val rolesAccessUnban : List<String>,
    @SerialName("unban_channel_id")
    val unbanChannel: String, )

@Serializable
data class ConfigNoticeOfDeparture(
    @SerialName("decision_channel_id")
    val descisionChannel: String,
    @SerialName("notice_channel_id")
    val noticeChannel: String,
    @SerialName("roles_access_notices")
    val rolesAccess: List<String>,
    @SerialName("check_type")
    val checkUnit: String,
    @SerialName("check_rate")
    val checkRate: Int)

@Serializable
data class ConfigRegulars(
    @SerialName("create_example_configuration")
    val createExample: Boolean,
    @SerialName("only_load_certain_folder")
    val onlyLoad: Boolean,
    @SerialName("only_load_folders")
    val loadFolders: List<String>)