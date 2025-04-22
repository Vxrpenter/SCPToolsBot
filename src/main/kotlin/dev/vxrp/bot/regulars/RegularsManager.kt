package dev.vxrp.bot.regulars
import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.api.sla.cedmod.Cedmod
import dev.vxrp.bot.regulars.data.RegularDatabaseEntry
import dev.vxrp.bot.regulars.data.RegularsConfigRole
import dev.vxrp.bot.regulars.enums.RequirementType
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.XPDatabaseHandler
import dev.vxrp.database.enums.AuthType
import dev.vxrp.database.tables.RegularsTable
import dev.vxrp.database.tables.UserTable
import dev.vxrp.util.Timer
import dev.vxrp.util.regularsScope
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class RegularsManager(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(RegularsManager::class.java)

    init {
        RegularsFileHandler(config, translation)
    }

    fun syncRegulars(userId: String, group: String) {
        val configQuery = RegularsFileHandler(config, translation).query()

        var groupRoleId: String? = null
        var roleId: String? = null

        for (folderGroup in configQuery) {
            if (folderGroup.manifest.name != group) continue

            if (folderGroup.manifest.customRole.use) groupRoleId = folderGroup.manifest.customRole.id

            for (role in folderGroup.config.roles) {
                roleId = role.id
                break
            }
            break
        }

        RegularsTable().addToDatabase(userId, true, group, groupRoleId, roleId!!, 0.0, LocalDate.now().toString(), 0)
    }

    fun reactivateSync(userId: String) {
        RegularsTable().setActive(userId, true)
    }

    fun deactivateSync(userId: String) {
        RegularsTable().setActive(userId, false)
    }

    fun removeSync(userId: String) {
        RegularsTable().delete(userId)
    }

    fun spinUpChecker() {
        if (!config.settings.regulars.active) return

        Timer().runWithTimer(2.hours, regularsScope) { checkerTask() }
    }

    private suspend fun checkerTask() {
        logger.info("Starting regulars checker, processing units starting...")

        for (regular in RegularsTable().getAllEntrys()) {
            val lastCheckedDate = LocalDate.parse(regular.lastCheckedDate)
            if (lastCheckedDate == LocalDate.now()) continue

            if (!UserTable().exists(regular.id)) {
                RegularsTable().delete(regular.id)
                logger.warn("Could not retrieve user: ${regular.id}'s verification data, deleting their regular data because of it being invalid")
                continue
            }

            val role = getRole(regular)
            role ?: run {
                logger.error("Regulars config does not match up to the database, players have roles in their registry that do not exist anymore")
                return
            }

            when(RequirementType.valueOf(role.requirementType)) {
                RequirementType.PLAYTIME -> {
                    if (!config.settings.cedmod.active) {
                        logger.error("Could not correctly process regulars with setting 'PLAYTIME', activate cedmod integration!")
                        return
                    }

                    if (!checkPlaytime(regular, lastCheckedDate)) break

                    checkRoles(regular.id, regular.groupRoleId, regular.roleId)
                }

                RequirementType.XP -> {
                    if (!config.settings.xp.active) {
                        logger.error("Could not correctly process regulars with setting 'XP', activate xp integration!")
                        return
                    }

                    if (!checkLevel(regular, role)) break

                    checkRoles(regular.id, regular.groupRoleId, regular.roleId)
                }

                RequirementType.BOTH -> {
                    if (!config.settings.cedmod.active || !config.settings.xp.active) {
                        logger.error("Could not correctly process regulars with setting 'BOTH', activate cedmod and xp integration!")
                        return
                    }

                    if (!checkPlaytime(regular, lastCheckedDate) || !checkLevel(regular, role)) break

                    checkRoles(regular.id, regular.groupRoleId, regular.roleId)
                }
            }

            RegularsTable().setLastCheckedDate(regular.id, LocalDate.now().toString())
            delay(10.seconds)
        }
    }

    private fun checkPlaytime(regular: RegularDatabaseEntry, lastCheckedDate: LocalDate): Boolean {
        val cedmod = Cedmod(config.settings.cedmod.instance, config.settings.cedmod.api)
        val steamId = UserTable().getSteamId(regular.id)

        if (regular.playtime == 0.0) {
            val player = cedmod.playerQuery(q = "$steamId@steam", activityMin = 365, basicStats = true)

            RegularsTable().setPlaytime(regular.id, player.players[0].activity)
            RegularsTable().setLastCheckedDate(regular.id, LocalDate.now().toString())
            logger.info("Updated user: ${regular.id}'s regular data for the first time, new playtime: ${player.players[0].activity}")

            return true
        }

        val activityMin = lastCheckedDate.until(LocalDate.now()).days
        if (activityMin == 0) return false

        val player = cedmod.playerQuery(q = "$steamId@steam", activityMin = activityMin, basicStats = true)
        val currentPlaytime = RegularsTable().getPlaytime(regular.id)

        val newPlaytime = currentPlaytime+player.players[0].activity

        RegularsTable().setPlaytime(regular.id, newPlaytime)
        logger.info("Updated user: ${regular.id}'s regular data by adding: ${player.players[0].activity} to their already existing playtime of: $currentPlaytime")
        return true
    }

    private fun checkLevel(regular: RegularDatabaseEntry, role: RegularsConfigRole): Boolean {
        val steamId = UserTable().getSteamId(regular.id)
        val discordId = regular.id

        val xp: Int = when(AuthType.valueOf(config.settings.xp.authType)) {
            AuthType.STEAMID -> {
                XPDatabaseHandler(config).queryExperience(AuthType.STEAMID, steamId.toLong())
            }

            AuthType.DISCORD -> {
                XPDatabaseHandler(config).queryExperience(AuthType.DISCORD, discordId.toLong())
            }
        }

        val level = (-50 + sqrt(((4 * xp / config.settings.xp.additionalParameter) + 9500).toFloat()) / 2)

        if (level >= role.xpRequirements) return true
        RegularsTable().setLevel(regular.id, level.toInt())
        logger.info("Updated user: ${regular.id}'s regular data setting their level to: $level")
        return false
    }

    private suspend fun checkRoles(userId: String, groupRoleId: String?, roleId: String) {
        val guild = api.getGuildById(config.settings.guildId)!!
        val member = guild.retrieveMemberById(userId).await()

        member ?: run {
            logger.error("Could not find user: $userId")
            return
        }

        var containGroupRole = false
        var containsRole = false

        for (role in member.roles) {
            if (groupRoleId != null && role.id == groupRoleId) containGroupRole = true

            if (role.id == roleId) containsRole = true
        }

        if (!containGroupRole) {
            val groupRole = api.getRoleById(groupRoleId!!)
            groupRole ?: run {
                logger.error("Could not correctly find group role: $groupRoleId")
                return
            }

            guild.addRoleToMember(member, groupRole).queue()
            logger.info("Updated playtime group role of user: $userId to $groupRole")
        }

        if (!containsRole) {
            val role = api.getRoleById(roleId)
            role ?: run {
                logger.error("Could not correctly find role: $groupRoleId")
                return
            }

            guild.addRoleToMember(member, role).queue()
            logger.info("Updated playtime role of user: $userId to $roleId")
        }
    }

    private fun getRole(regular: RegularDatabaseEntry):  RegularsConfigRole? {
        val configQuery = RegularsFileHandler(config, translation).query()

        for (config in configQuery) {
            if (config.manifest.name != regular.group) continue

            for (role in config.config.roles) {
                if (role.id != regular.roleId) continue

                return role
            }
        }

        return null
    }
}