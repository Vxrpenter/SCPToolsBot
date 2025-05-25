package dev.vxrp.updates.data

import kotlinx.serialization.Serializable

@Serializable
data class Updates(
    val version: String,
    val settings: UpdatesSettings,
    val configurationUpdate: UpdatesConfigurationUpdate,
    val translationUpdates: UpdatesTranslationUpdates,
    val regularsUpdate: UpdatesRegularsUpdate,
    val additionalInformation: String
)

@Serializable
data class UpdatesSettings(
    var regenerateConfig: Boolean,
    var regenerateStatusSettings: Boolean,
    var regenerateTicketSettings: Boolean,
    var regenerateCommands: Boolean,
    var regenerateLaunchConfiguration: Boolean,

    var regenerateTranslations: Boolean
)

@Serializable
data class UpdatesConfigurationUpdate(
    val config: Boolean,
    val statusSettings: Boolean,
    val ticketSettings: Boolean,
    val extra: UpdatesConfigurationUpdateExtra
)

@Serializable
data class UpdatesConfigurationUpdateExtra(
    val commands: Boolean,
    val launchConfiguration: Boolean
)

@Serializable
data class UpdatesTranslationUpdates(
    val enUs: Boolean,
    val deDe: Boolean
)

@Serializable
data class UpdatesRegularsUpdate(
    val config: Boolean,
    val manifest: Boolean
)