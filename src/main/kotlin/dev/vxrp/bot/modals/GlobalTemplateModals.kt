/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

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