package dev.vxrp.bot.events.stringSelectMenus

import dev.vxrp.bot.modals.ApplicationModals
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

class ApplicationStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.selectMenu.id?.startsWith("application_activation_add") == true) {
            event.replyModal(ApplicationModals(translation).chooseCountModal( event.selectedOptions[0].value)).queue()
        }

        if (event.selectMenu.id?.startsWith("application_activation_remove") == true) {
            //TODO
        }
    }
}