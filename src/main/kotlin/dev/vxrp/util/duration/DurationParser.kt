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

package dev.vxrp.util.duration

import dev.vxrp.util.duration.enums.DurationType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class DurationParser {
    fun parse(int: Int, durationType: DurationType): Duration {
        return when (durationType) {
            DurationType.NANOSECONDS -> int.nanoseconds
            DurationType.MICROSECONDS -> int.microseconds
            DurationType.MILLISECONDS -> int.milliseconds
            DurationType.SECONDS -> int.seconds
            DurationType.MINUTES -> int.minutes
            DurationType.HOURS -> int.hours
            DurationType.DAYS -> int.days
        }
    }
}