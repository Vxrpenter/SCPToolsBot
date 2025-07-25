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

package dev.vxrp.bot.regulars.handler

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.bot.regulars.data.RegularDatabaseEntry
import dev.vxrp.bot.regulars.data.RegularsConfigRole
import dev.vxrp.bot.regulars.enums.RequirementType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.XPDatabaseHandler
import dev.vxrp.database.enums.AuthType
import dev.vxrp.database.tables.database.RegularsTable
import dev.vxrp.database.tables.database.UserTable
import io.github.vxrpenter.cedmod.Cedmod
import io.github.vxrpenter.cedmod.exceptions.CallFailureException
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

class RegularsCheckerHandler(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(RegularsCheckerHandler::class.java)

    suspend fun checkerTask() {
        logger.info("Starting regulars checker, processing units starting...")

        for (regular in RegularsTable().getAllEntrys()) {
            if (!checkRegular(regular)) continue
            delay(10.seconds)
        }
    }

    suspend fun checkRegular(regular: RegularDatabaseEntry): Boolean {
        val lastCheckedDate: LocalDate? = null
        if (regular.lastCheckedDate != null) LocalDate.parse(regular.lastCheckedDate)

        if (!UserTable().exists(regular.id)) {
            RegularsTable().delete(regular.id)
            logger.warn("Could not retrieve user: ${regular.id}'s verification data, deleting their regular data because of it being invalid")
            return false
        }

        val role = getRole(regular)
        role ?: run {
            RegularsTable().delete(regular.id)
            logger.error("Regular user: ${regular.id}'s role entrys do not match up to the regulars config, their entry will be removed")
            return false
        }

        val steamId = UserTable().getSteamId(regular.id)
        steamId ?: run {
            RegularsTable().delete(regular.id)
            logger.error("Could not find user entry of regular user: ${regular.id}, deleting entry")
            return false
        }

        when(RequirementType.valueOf(role.requirementType)) {
            RequirementType.PLAYTIME -> {
                if (!config.settings.cedmod.active) {
                    logger.error("Could not correctly process regulars with setting 'PLAYTIME', activate cedmod integration!")
                    return false
                }

                if (!checkPlaytime(regular, steamId, lastCheckedDate)) return false

                checkRoles(regular.id, regular.groupRoleId, regular.roleId)
            }

            RequirementType.XP -> {
                if (!config.settings.xp.active) {
                    logger.error("Could not correctly process regulars with setting 'XP', activate xp integration!")
                    return false
                }

                if (!checkLevel(regular, steamId, role)) return false

                checkRoles(regular.id, regular.groupRoleId, regular.roleId)
            }

            RequirementType.BOTH -> {
                if (!config.settings.cedmod.active || !config.settings.xp.active) {
                    logger.error("Could not correctly process regulars with setting 'BOTH', activate cedmod and xp integration!")
                    return false
                }

                if (!checkPlaytime(regular, steamId, lastCheckedDate) && !checkLevel(regular, steamId, role)) return false

                checkRoles(regular.id, regular.groupRoleId, regular.roleId)
            }
        }

        RegularsTable().setLastCheckedDate(regular.id, LocalDate.now().toString())
        return true
    }

    private fun checkPlaytime(regular: RegularDatabaseEntry, steamId: String, lastCheckedDate: LocalDate?): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        if (lastCheckedDate?.format(formatter) == LocalDate.now().format(formatter)) return false
        val cedmod = Cedmod(config.settings.cedmod.instance, config.settings.cedmod.api)

        try {
            if (regular.playtime == 0.0) {
                val player = cedmod.playerQuery(q = "$steamId@steam", activityMin = 365, basicStats = true)

                RegularsTable().setPlaytime(regular.id, player.players[0].activity)
                RegularsTable().setLastCheckedDate(regular.id, LocalDate.now().toString())
                logger.info("Updated user: ${regular.id}'s regular playtime data for the first time, new playtime: ${player.players[0].activity}")

                return true
            }

            if (lastCheckedDate == null) return false
            val activityMin = lastCheckedDate.until(LocalDate.now()).days

            val player = cedmod.playerQuery(q = "$steamId@steam", activityMin = activityMin, basicStats = true)
            val currentPlaytime = RegularsTable().getPlaytime(regular.id)

            val newPlaytime = currentPlaytime+player.players[0].activity

            RegularsTable().setPlaytime(regular.id, newPlaytime)
            logger.info("Updated user: ${regular.id}'s regular playtime data by adding: ${player.players[0].activity} to their already existing playtime of: $currentPlaytime, result: $newPlaytime")
            return true
        } catch (e: CallFailureException) {
            logger.error("Could not correctly execute cedmod call ${e.message}")
            return false
        }
    }

    private fun checkLevel(regular: RegularDatabaseEntry, steamId: String, role: RegularsConfigRole): Boolean {
        val discordId = regular.id

        val xp: Int = when(AuthType.valueOf(config.settings.xp.authType)) {
            AuthType.STEAM -> {
                XPDatabaseHandler(config).queryExperience(AuthType.STEAM, steamId.toLong())
            }

            AuthType.DISCORD -> {
                XPDatabaseHandler(config).queryExperience(AuthType.DISCORD, discordId.toLong())
            }
        }

        val level = (-50 + sqrt(((4 * xp / config.settings.xp.additionalParameter) + 9500).toFloat()) / 2)

        if (level >= role.xpRequirements) return true
        RegularsTable().setLevel(regular.id, level.toInt())
        logger.info("Updated user: ${regular.id}'s regular xp data setting their level to: $level")
        return false
    }

    private suspend fun checkRoles(userId: String, groupRoleId: String?, roleId: String) {
        val guild = api.getGuildById(config.settings.guildId)!!
        val member = guild.retrieveMemberById(userId).await()

        member ?: run {
            logger.error("Could not grant user: $userId's regular role, do they exist?")
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
                logger.error("Could not correctly find group role: $groupRoleId, does it exist?")
                return
            }

            guild.addRoleToMember(member, groupRole).queue()
            logger.info("Updated regular group role of user: $userId to $groupRole")
        }

        if (!containsRole) {
            val role = api.getRoleById(roleId)
            role ?: run {
                logger.error("Could not correctly find role: $roleId, does it exist?")
                return
            }

            guild.addRoleToMember(member, role).queue()
            logger.info("Updated regular role of user: $userId to $roleId")
        }
    }

    private fun getRole(regular: RegularDatabaseEntry):  RegularsConfigRole? {
        val configQuery = RegularsFileHandler(config).query()

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