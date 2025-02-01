package dev.vxrp.bot.events.stringSelectMenus

import dev.vxrp.bot.application.ApplicationManager
import dev.vxrp.bot.modals.ApplicationModalSave
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

class ApplicationStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.selectMenu.id?.startsWith("application_activation_add") == true) {
            event.replyModal(ApplicationModalSave(translation).chooseCountModal(event.selectedOptions[0].value, event.selectMenu.id?.split(":")?.get(2)!!)).queue()
        }

        if (event.selectMenu.id?.startsWith("application_activation_remove") == true) {
            val roleId = event.selectedOptions[0].value
            val messageId = event.selectMenu.id!!.split(":")[2]

            event.deferEdit().queue()
            ApplicationManager(config, translation).editActivationMessage(event.user.id, roleId, event.channel.asTextChannel(), messageId, state = false, member = 0)
        }
    }
}