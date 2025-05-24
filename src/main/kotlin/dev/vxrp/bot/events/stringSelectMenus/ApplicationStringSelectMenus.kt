package dev.vxrp.bot.events.stringSelectMenus

import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.application.ApplicationMessageHandler
import dev.vxrp.bot.modals.ApplicationTemplateModals
import dev.vxrp.bot.modals.TicketTemplateModals
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.ApplicationTypeTable
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

class ApplicationStringSelectMenus(val event: StringSelectInteractionEvent, val config: Config, val translation: Translation) {
    fun init() {
        if (event.selectMenu.id?.startsWith("application_activation_add") == true) {
            event.replyModal(ApplicationTemplateModals(translation).chooseCountModal(event.selectedOptions[0].value, event.selectMenu.id?.split(":")?.get(2)!!)).queue()
        }

        if (event.selectMenu.id?.startsWith("application_activation_remove") == true) {
            val roleId = event.selectedOptions[0].value
            val messageId = event.selectMenu.id!!.split(":")[2]

            event.deferEdit().queue()
            ApplicationMessageHandler(config, translation).editActivationMessage(event.user.id, roleId, event.channel.asTextChannel(), messageId, state = false, member = 0)
        }

        if (event.selectMenu.id?.startsWith("application_position") == true) {
            if (!ApplicationTypeTable().query(event.selectedOptions[0].value)!!.active) {
                event.reply_("Position currently not active").setEphemeral(true).queue()
            } else {
                event.replyModal(TicketTemplateModals(translation).supportApplicationModal(event.selectedOptions[0].value)).queue()
            }
        }
    }
}