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

package dev.vxrp.database.tables.xp

import org.jetbrains.exposed.sql.Table

class PlayerInfoTable {
    object PlayerInfoSteam : Table("playerinfo_Steam") {
        val id = long("id")
        val xp = integer("xp").default(0)
        val nickname = varchar("nickname", 64)

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }

    object PlayerInfoDiscord: Table("playerinfo_Discord") {
        val id = long("id")
        val xp = integer("xp").default(0)
        val nickname = varchar("nickname", 64)

        override val primaryKey: PrimaryKey
            get() = PrimaryKey(id)
    }
}