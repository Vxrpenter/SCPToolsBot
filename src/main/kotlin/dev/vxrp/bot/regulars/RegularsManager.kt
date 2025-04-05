package dev.vxrp.bot.regulars
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.RegularsTable
import java.time.LocalDate

class RegularsManager(val config: Config, val translation: Translation) {


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
}