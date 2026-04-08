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

package dev.vxrp.util.launch.data

import dev.vxrp.configlite.ConfigLite
import dev.vxrp.configuration.storage.ConfigPaths
import kotlinx.serialization.Serializable

@Serializable
data class LaunchConfiguration(
    val options: LaunchConfigurationOptions,
    val order: List<LaunchConfigurationOrder>,
) {
    companion object {
        val instance by lazy {
            ConfigLite.load<LaunchConfiguration>(ConfigPaths().launchConfigurationPath.fileName.toString())
        }

    }
}

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