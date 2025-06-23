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
    val regenerateConfig: Boolean,
    val regenerateStatusSettings: Boolean,
    val regenerateTicketSettings: Boolean,
    val regenerateCommands: Boolean,
    val regenerateLaunchConfiguration: Boolean,

    val regenerateTranslations: Boolean
)

@Serializable
data class UpdatesConfigurationUpdate(
    val config: UpdatesConfigurationSegment,
    val statusSettings: UpdatesConfigurationSegment,
    val ticketSettings: UpdatesConfigurationSegment,
    val extra: UpdatesConfigurationUpdateExtra
)

@Serializable
data class UpdatesConfigurationUpdateExtra(
    val commands: UpdatesConfigurationSegment,
    val launchConfiguration: UpdatesConfigurationSegment
)

@Serializable
data class UpdatesTranslationUpdates(
    val enUs: UpdatesConfigurationSegment,
    val deDe: UpdatesConfigurationSegment
)

@Serializable
data class UpdatesRegularsUpdate(
    val config: UpdatesConfigurationSegment,
    val manifest: UpdatesConfigurationSegment
)

@Serializable
data class UpdatesConfigurationSegment(
    val changed: Boolean,
    val filename: String,
    val location: String,
    val upstream: String,
)