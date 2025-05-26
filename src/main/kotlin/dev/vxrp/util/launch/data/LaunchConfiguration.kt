package dev.vxrp.util.launch.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchConfiguration(
    val options: LaunchConfigurationOptions,
    @SerialName("launch_order")
    val order: List<LaunchConfigurationOrder>,
)

@Serializable
data class LaunchConfigurationOptions(
    @SerialName("ignore_broken_entries")
    val ignoreBrokenEntries: Boolean
)

@Serializable
data class LaunchConfigurationOrder(
    val id: String,
    val engage: Boolean,
    val sections: List<LaunchConfigurationSection>? = null
)

@Serializable
data class LaunchConfigurationSection(
    val id: String,
    val engage: Boolean,
    val logAction: Boolean
)