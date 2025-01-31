package dev.vxrp.bot.application.data

data class ApplicationType (
    val pos: Int,
    val roleId: String,
    val name: String,
    val description: String,
    val emoji: String,
    val state: Boolean,
    val initializer: String?,
    val member: Int
)