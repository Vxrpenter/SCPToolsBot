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