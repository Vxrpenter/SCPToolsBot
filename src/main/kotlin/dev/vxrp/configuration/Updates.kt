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
import kotlinx.serialization.Serializable

@Serializable
data class Updates(
    val version: String,
    val configurationUpdate: List<UpdatesConfigurationSegment>,
    val translationUpdates: List<UpdatesConfigurationSegment>,
    val regularsUpdate: List<UpdatesConfigurationSegment>,
    val additionalInformation: String
) { companion object { val instance by lazy { ConfigLite.load<Updates>("updates.json") } } }

@Serializable
data class UpdatesConfigurationSegment(
    val changed: Boolean,
    val regenerate: Boolean,
    val type: String,
    val filename: String,
    val location: String,
    val upstream: String,
)