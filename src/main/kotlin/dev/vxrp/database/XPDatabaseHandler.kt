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

package dev.vxrp.database

import com.mysql.cj.jdbc.exceptions.CommunicationsException
import dev.vxrp.configuration.data.Config
import dev.vxrp.database.enums.AuthType
import dev.vxrp.database.tables.xp.PlayerInfoTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class XPDatabaseHandler(val config: Config) {
    var database: Database? = null
    private val logger = LoggerFactory.getLogger(XPDatabaseHandler::class.java)

    fun connectToDatabase(): Database? {
        if (!config.settings.xp.active) return null

        if (config.settings.database.dataUsePredefined == "NONE" && config.settings.database.customUrl == config.settings.xp.databaseAddress)  {
            logger.warn("Found Custom database and Xp database to be the same. Cancelling connection to xp database")
            return null
        }

        val url = "jdbc:mysql://${config.settings.xp.databaseAddress}"

        return try {
            Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", config.settings.xp.databaseUser, config.settings.xp.databasePassword)
        } catch (_: CommunicationsException) {
            logger.error("Could not connect to XP database, all xp database action will fall back to main database, please try fixing your connection")
            null
        }
    }

    fun queryExperience(authType: AuthType, userId: Long): Int {
        return when(authType) {
            AuthType.STEAM -> {
                steamTableTransaction(userId)
            }

            AuthType.DISCORD -> {
                discordTableTransaction(userId)
            }
        }
    }

    private fun steamTableTransaction(userId: Long): Int {
        var xp: Int? = null

        transaction(database) {
            PlayerInfoTable.PlayerInfoSteam.selectAll()
                .where {PlayerInfoTable.PlayerInfoSteam.id eq userId}
                .forEach {
                    xp = it[PlayerInfoTable.PlayerInfoSteam.xp]
                }
        }

        return xp!!
    }

    private fun discordTableTransaction(userId: Long): Int {
        var xp: Int? = null

        transaction(database) {
            PlayerInfoTable.PlayerInfoDiscord.selectAll()
                .where {PlayerInfoTable.PlayerInfoDiscord.id eq userId}
                .forEach {
                    xp = it[PlayerInfoTable.PlayerInfoDiscord.xp]
                }
        }

        return xp!!
    }
}