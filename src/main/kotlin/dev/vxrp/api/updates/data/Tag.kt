package dev.vxrp.api.updates.data

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val ref: String,
    val url: String
)
