package dev.vxrp.bot

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import dev.vxrp.api.cedmod.Cedmod
import dev.vxrp.bot.commands.CommandListener
import dev.vxrp.bot.commands.CommandManager
import dev.vxrp.bot.events.ButtonListener
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.sqlite.SqliteManager
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class BotManager(val config: Config, val translation: Translation) {
    init {
        println(Cedmod("https://ravenblack.cmod.app/",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJDbGllbnQiLCJqdGkiOiI4ZGM3N2QwOS1kYTUzLTQ2NjgtYThiNC1kZWE4NDIzZDhjM2YiLCJpYXQiOiIxMS8yMi8yMDI0IDE0OjM3OjU2IiwiVXNlcklkIjoiMmQ4MTRhNGQtZTIzMy00YTlkLTljMTQtYjkzMjFlODdjN2VkIiwiRGlzcGxheU5hbWUiOiJCb3QgRGV2ZWxvcG1lbnQgVG9rZW4iLCJVc2VyTmFtZSI6IkJvdCBEZXZlbG9wbWVudCBUb2tlbiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWUiOiJCb3QgRGV2ZWxvcG1lbnQgVG9rZW4iLCJTZXNzaW9uVG9rZW4iOiJrUXQwTTZaZ2F5Yl9RQXp0enNwbC1YOTJPQmFFUENTWW9jOGdKLXhIUDRIUzRxazNkSkRRLTl4djZtd1JXcm9jIiwiU2Vzc2lvbklkIjoiQXV0aGVudGljYXRpb25TZXNzaW9uLXNJVHRHbmNYbzJ5bGtxN3Bwd1RmUXJSVlJLbW1EdFdYMGl0VjItSDZLcGlCdHEzcExQT3h1YXFUMndKei1Da0kiLCJleHAiOjE4MjY4OTQyNzYsImlzcyI6ImNlZG1vZCIsImF1ZCI6IlVzZXIifQ.dffwaHEICgsQgWKl4DDRIaYcj4q3fnZRbbXyjdg_5-E",
            60, 60).instanceGetStats())

        val definedIntents = listOf(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)

        val api = light(config.token, enableCoroutines=true) {
            intents += definedIntents
            setActivity(Activity.of(
                ActivityType.PLAYING.takeIf { !enumContains<ActivityType>(config.activityType) } ?: ActivityType.valueOf(config.activityType),
                config.activityContent))
            disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
        }
        val guild = api.awaitReady().getGuildById(config.guildId)

        api.addEventListener(
            CommandListener(api, config, translation),
            ButtonListener(api, config, translation),
        )

        CommandManager(api, config, "/configs/commands.json").registerCommands()
        SqliteManager(config,"database", "data.db")
    }

    private inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name}
    }
}