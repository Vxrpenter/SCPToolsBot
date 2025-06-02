package dev.vxrp.bot.application

import dev.vxrp.bot.application.data.ApplicationType
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation

val applicationTypeMap: HashMap<String, MutableList<ApplicationType>> = hashMapOf()

class ApplicationManager(val config: Config, val translation: Translation) {

    fun changeApplicationType(userID: String, roleID: String, name: String? = null, description: String? = null, emoji: String? = null, state: Boolean? = null, initializer: String? = null, member: Int? = null) {
        for (type in applicationTypeMap[userID]!!) {
            if (type.roleId != roleID) continue

            var typeName = type.name
            var typeDescription = type.description
            var typeEmoji = type.emoji
            var typeState = type.state
            var typeInitializer = type.initializer
            var typeMember = type.member

            if (name != null) typeName = name
            if (description != null) typeDescription = description
            if (emoji != null) typeEmoji = emoji
            if (state != null) typeState = state
            if (initializer != null) typeInitializer = initializer
            if (member != null) typeMember = member

            val applicationTypeList = applicationTypeMap[userID]!!

            applicationTypeList[type.pos] = ApplicationType(type.pos, type.roleId, typeName, typeDescription, typeEmoji, typeState, typeInitializer, typeMember)

            break
        }
    }
}