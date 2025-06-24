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

package dev.vxrp.bot.events.modals

import dev.vxrp.bot.application.handler.ApplicationMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

class ApplicationModals(val event: ModalInteractionEvent, val config: Config, val translation: Translation) {
    init {
        if (event.modalId.startsWith("application_choose_count")) {
            val roleId = event.modalId.split(":")[1]
            val messageId = event.modalId.split(":")[2]
            val members = event.values[0].asString.toIntOrNull()

            event.deferEdit().queue()
            ApplicationMessageHandler(config, translation).editActivationMessage(event.user.id, roleId, event.channel.asTextChannel(), messageId, member = members, state = true, initializer = event.user.id)
        }
    }
}