package dev.vxrp.util.launch.data

import kotlinx.serialization.Serializable

@Serializable
data class LaunchConfiguration(
    val options: LaunchConfigurationOptions,
    val order: List<LaunchConfigurationOrder>,
)

@Serializable
data class LaunchConfigurationOptions(
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