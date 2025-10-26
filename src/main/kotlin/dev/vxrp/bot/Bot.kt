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

package dev.vxrp.bot

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.configuration.Config
import dev.vxrp.configuration.Translation
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class Bot(private val config: Config, private val translation: Translation) {
    init {
        val bot = light(config.settings.token, enableCoroutines = true) {
            // Set intents and disable flags
            intents += listOf(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)

            // Setting activity
            setActivity(Activity.of(Activity.ActivityType.PLAYING.takeIf {
                !enumContains<Activity.ActivityType>(config.settings.activityType)
            } ?: Activity.ActivityType.valueOf(config.settings.activityType), config.settings.activityContent))

            // Activating the listeners
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean = enumValues<T>().any { it.name == name }
}