/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.configuration

import dev.vxrp.configlite.ConfigLite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Config(
    val settings: Settings,
    val status: Status,
    val ticket: Ticket,
    val extra: ConfigExtra
)

data class ConfigExtra(
    val commands: Commands
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
    val updates: ConfigUpdates,
    @SerialName("activity_type")
    val activityType: String,
    @SerialName("activity_content")
    val activityContent: String,
    val database: ConfigDatabase,
    val webserver: ConfigWebserver,
    val cedmod: ConfigCedmod,
    @SerialName("XP")
    val xp: ConfigXP,
    val verify: ConfigVerify,
    @SerialName("notice_of_departure")
    val noticeOfDeparture: ConfigNoticeOfDeparture,
    val regulars: ConfigRegulars
)

@Serializable
data class ConfigUpdates(
    @SerialName("ignore_beta")
    val ignoreBeta: Boolean,
    @SerialName("ignore_alpha")
    val ignoreAlpha: Boolean
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
data class ConfigWebserver(
    val active: Boolean,
    val port: Int,
    @SerialName("redirect_uri")
    val redirectUri: String,
    val uri: String
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
data class ConfigXP(
    val active: Boolean,
    @SerialName("database_address")
    val databaseAddress : String,
    @SerialName("database_user")
    val databaseUser: String,
    @SerialName("database_password")
    val databasePassword: String,
    @SerialName("auth_type")
    val authType: String,
    @SerialName("additional_parameter")
    val additionalParameter: Int
)

@Serializable
data class ConfigVerify(
    val active: Boolean,
    @SerialName("oauth_link")
    val oauthLink: String,
    @SerialName("verify_log_channel")
    val verifyLogChannel: String
)

@Serializable
data class ConfigNoticeOfDeparture(
    val active: Boolean,
    @SerialName("date_formatting")
    val dateFormatting: String,
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
    val active: Boolean,
    @SerialName("create_example_configuration")
    val createExample: Boolean,
    @SerialName("only_load_certain_folder")
    val onlyLoad: Boolean,
    @SerialName("only_load_folders")
    val loadFolders: List<String>
)  {
    companion object {
        val instance by lazy {
            ConfigLite.load<Translation>("config.yml")
        }
    }
}