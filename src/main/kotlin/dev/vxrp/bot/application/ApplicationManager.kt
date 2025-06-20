package dev.vxrp.bot.application

import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.applicationTypeSet

class ApplicationManager(val config: Config, val translation: Translation) {

    fun changeApplicationType(roleID: String, name: String? = null, description: String? = null, emoji: String? = null, state: Boolean? = null, initializer: String? = null, member: Int? = null) {
        applicationTypeSet.filter { it.roleId == roleID }.forEach {
            var typeName = it.name
            var typeDescription = it.description
            var typeEmoji = it.emoji
            var typeState = it.state
            var typeInitializer = it.initializer
            var typeMember = it.member

            if (name != null) typeName = name
            if (description != null) typeDescription = description
            if (emoji != null) typeEmoji = emoji
            if (state != null) typeState = state
            if (initializer != null) typeInitializer = initializer
            if (member != null) typeMember = member

            it.name = typeName
            it.description = typeDescription
            it.emoji = typeEmoji
            it.state = typeState
            it.initializer = typeInitializer
            it.member = typeMember
        }
    }
}