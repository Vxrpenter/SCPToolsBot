package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class GlobalTemplateModals(val translation: Translation) {
    fun reasonModal(id: String): Modal {
        return Modal.create(id, translation.permissions.modalReasonTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "reason",
                    label = translation.permissions.modalReasonEnterReasonTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    requiredLength = 1..1000,
                    placeholder = translation.permissions.modalReasonEnterReasonPlaceholder).build()
            )
        ).build()
    }
}