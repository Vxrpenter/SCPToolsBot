package dev.vxrp.bot.regulars
import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.api.sla.cedmod.Cedmod
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.RegularsTable
import dev.vxrp.database.tables.UserTable
import dev.vxrp.util.Timer
import dev.vxrp.util.regularsScope
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.time.LocalDate
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

        RegularsTable().addToDatabase(userId, true, group, groupRoleId, roleId!!, 0.0, LocalDate.now().toString())
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
        if (!config.settings.cedmod.active) return

        Timer().runWithTimer(2.hours, regularsScope) { checkerTask() }
    }

    private suspend fun checkerTask() {
        logger.info("Starting regulars checker, processing units starting...")

        val currentDate = LocalDate.now()

        for (regular in RegularsTable().getAllEntrys()) {
            val lastCheckedDate = LocalDate.parse(regular.lastCheckedDate)
            if (lastCheckedDate == currentDate) continue

            if (!UserTable().exists(regular.id)) {
                RegularsTable().delete(regular.id)
                logger.warn("Could not retrieve user: ${regular.id}'s verification data, deleting their regular data because of it being invalid")
                continue
            }

            val cedmod = Cedmod(config.settings.cedmod.instance, config.settings.cedmod.api)
            val steamId = UserTable().getSteamId(regular.id)

            if (regular.playtime == 0.0) {
                val player = cedmod.playerQuery(q = "$steamId@steam", activityMin = 365, basicStats = true)

                RegularsTable().setPlaytime(regular.id, player.players[0].activity)
                RegularsTable().setLastCheckedDate(regular.id, currentDate.toString())
                logger.info("Updated user: ${regular.id}'s regular data for the first time, new playtime: ${player.players[0].activity}")

                checkRoles(regular.id, regular.groupRoleId, regular.roleId)
                return
            }

            val activityMin = lastCheckedDate.until(currentDate).days
            if (activityMin == 0) return

            val player = cedmod.playerQuery(q = "$steamId@steam", activityMin = activityMin, basicStats = true)
            val currentPlaytime = RegularsTable().getPlaytime(regular.id)

            val newPlaytime = currentPlaytime+player.players[0].activity

            RegularsTable().setPlaytime(regular.id, newPlaytime)
            RegularsTable().setLastCheckedDate(regular.id, currentDate.toString())
            logger.info("Updated user: ${regular.id}'s regular data by adding: ${player.players[0].activity} to their already existing playtime of: $currentPlaytime")

            checkRoles(regular.id, regular.groupRoleId, regular.roleId)
            delay(10.seconds)
        }
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
}