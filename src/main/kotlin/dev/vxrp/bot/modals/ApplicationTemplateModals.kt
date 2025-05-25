package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class ApplicationTemplateModals(val translation: Translation) {
    fun chooseCountModal(roleId: String, modalId: String): Modal {
        return Modal.create("application_choose_count:$roleId:$modalId", translation.application.modalChooseCountTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "application_choose_count_number",
                    label = translation.application.modalChooseCountNumberTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    requiredLength = 1..1000,
                    placeholder = translation.application.modalChooseCountNumberPlaceholder).build()
            )
        ).build()
    }
}