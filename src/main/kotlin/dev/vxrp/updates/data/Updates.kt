package dev.vxrp.updates.data

import kotlinx.serialization.Serializable

@Serializable
data class Updates(
    val version: String,
    val settings: UpdatesSettings,
    val configurationUpdate: List<UpdatesConfigurationSegment>,
    val translationUpdates: List<UpdatesConfigurationSegment>,
    val regularsUpdate: List<UpdatesConfigurationSegment>,
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
data class UpdatesConfigurationSegment(
    val changed: Boolean,
    val filename: String,
    val location: String,
    val upstream: String,
)