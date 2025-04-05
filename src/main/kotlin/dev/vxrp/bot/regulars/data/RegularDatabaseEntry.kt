package dev.vxrp.bot.regulars.data

data class RegularDatabaseEntry (
    val id: String,
    val active: Boolean,
    val group: String,
    val groupRoleId: String?,
    val roleId: String,
    val lastCheckedDate: String,
    val playtime: Double
)