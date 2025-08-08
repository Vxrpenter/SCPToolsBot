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

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import io.github.freya022.botcommands.api.core.JDAService
import io.github.freya022.botcommands.api.core.events.BReadyEvent
import io.github.freya022.botcommands.api.core.light
import io.github.freya022.botcommands.api.core.service.annotations.BService
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

@BService
class Bot(private val config: Config, private val translation: Translation) : JDAService() {

    override val intents: Set<GatewayIntent> = defaultIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
    override val cacheFlags: Set<CacheFlag> = setOf()

    override fun createJDA(event: BReadyEvent, eventManager: IEventManager) {
        light(
            config.settings.token,
            activity = Activity.of(ActivityType.PLAYING.takeIf {
                !enumContains<ActivityType>(config.settings.activityType)
            } ?: ActivityType.valueOf(config.settings.activityType), config.settings.activityContent)
        ) {
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
        }
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}

