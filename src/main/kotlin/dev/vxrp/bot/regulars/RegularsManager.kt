package dev.vxrp.bot.regulars
import dev.vxrp.bot.regulars.handler.RegularsCheckerHandler
import dev.vxrp.bot.regulars.handler.RegularsFileHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.regularsScope
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class RegularsManager(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(RegularsManager::class.java)

    init {
        RegularsFileHandler(config)
    }

    suspend fun syncRegulars(userId: String, group: String) {
        val configQuery = RegularsFileHandler(config).query()

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

        RegularsTable().addToDatabase(userId, true, group, groupRoleId, roleId!!, 0.0, 0, null)
        RegularsCheckerHandler(api, config, translation).checkRegular(RegularsTable().getEntry(userId)!!)
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

        Timer().runWithTimer(2.hours, regularsScope) { RegularsCheckerHandler(api, config, translation).checkerTask() }
    }
}