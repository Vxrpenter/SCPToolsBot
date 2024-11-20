package dev.vxrp.util

import dev.vxrp.configuration.loaders.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User

class TicketHandler(private val api: JDA, val config: Config, val creator: User) {
    init {
        val roles: ArrayList<Role> = ArrayList()

        for (role in config.support.rolesAccessSupport) {
            api.getRoleById(role)?.let { roles.add(it) }
        }
    }
}