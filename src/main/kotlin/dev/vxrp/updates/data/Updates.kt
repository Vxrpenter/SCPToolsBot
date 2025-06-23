package dev.vxrp.updates.data

import kotlinx.serialization.Serializable

@Serializable
data class Updates(
    val version: String,
    val configurationUpdate: List<UpdatesConfigurationSegment>,
    val translationUpdates: List<UpdatesConfigurationSegment>,
    val regularsUpdate: List<UpdatesConfigurationSegment>,
    val additionalInformation: String
)

@Serializable
data class UpdatesConfigurationSegment(
    val changed: Boolean,
    val regenerate: Boolean,
    val type: String,
    val filename: String,
    val location: String,
    val upstream: String,
)