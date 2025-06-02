package dev.vxrp.bot.application.data

data class ApplicationType (
    var pos: Int,
    var roleId: String,
    var name: String,
    var description: String,
    var emoji: String,
    var state: Boolean,
    var initializer: String?,
    var member: Int
)