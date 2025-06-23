/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.bot.regulars
import dev.vxrp.bot.regulars.handler.RegularsCheckerHandler
import dev.vxrp.bot.regulars.handler.RegularsFileHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.regularsScope
import net.dv8tion.jda.api.JDA
import kotlin.time.Duration.Companion.hours

class RegularsManager(val api: JDA, val config: Config, val translation: Translation) {
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
        if (!config.settings.regulars.active || RegularsTable().retrieveSerial() == 0L) return

        Timer().runWithTimer(
            2.hours,
            regularsScope
        ) { RegularsCheckerHandler(api, config, translation).checkerTask() }
    }
}