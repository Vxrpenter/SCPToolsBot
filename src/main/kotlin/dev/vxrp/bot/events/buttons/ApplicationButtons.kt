package dev.vxrp.bot.events.buttons

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import dev.vxrp.bot.application.ApplicationMessageHandler
import dev.vxrp.bot.application.data.ApplicationType
import dev.vxrp.bot.application.handler.ApplicationManager
import dev.vxrp.bot.application.handler.applicationTypeMap
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.util.color.ColorTool
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import org.slf4j.LoggerFactory

class ApplicationButtons(val event: ButtonInteractionEvent, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(ApplicationButtons::class.java)

    suspend fun init() {
        if (event.button.id?.startsWith("application_activation_add") == true && !nullCheck()) {
            val embed = Embed {
                color = 0x2ECC70
                title = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionTitle)
                description = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("application_activation_add:${event.user.id}:${event.message.id}").also {
                    for (application in applicationTypeMap[event.user.id]!!) {
                        it.addOption(application.name, application.roleId, application.description, Emoji.fromFormatted(application.emoji))
                    }
                }.build()
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("application_activation_remove") == true && !nullCheck()) {
            val embed = Embed {
                color = 0xE74D3C
                title = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionTitle)
                description = ColorTool().useCustomColorCodes(translation.application.embedChoosePositionBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("application_activation_remove:${event.user.id}:${event.messageId}").also {
                    for (application in applicationTypeMap[event.user.id]!!) {
                        it.addOption(application.name, application.roleId, application.description, Emoji.fromFormatted(application.emoji))
                    }
                }.build()
            ).setEphemeral(true).queue()
        }

        if (event.button.id?.startsWith("application_activation_complete_setup") == true && !nullCheck()) {
            if (config.ticket.settings.applicationMessageChannel != "") {
                event.message.delete().queue()
                ApplicationMessageHandler(config, translation).sendApplicationMessage(event.jda, event.user.id, event.jda.getTextChannelById(config.ticket.settings.applicationMessageChannel)!!, true)
                val embed = Embed {
                    color = 0x2ECC70
                    title = ColorTool().useCustomColorCodes(translation.application.embedApplicationActivatedTitle)
                    description = ColorTool().useCustomColorCodes(translation.application.embedApplicationActivatedBody)
                }
                event.reply_("", listOf(embed)).setEphemeral(true).queue()
            } else {
                logger.warn("Could not complete application setup, add channel id in the config to fix")
            }
        }

        if (event.button.id?.startsWith("application_deactivate") == true) {
            if (config.ticket.settings.applicationMessageChannel != "") {
                event.message.delete().queue()

                val listOfTypes = mutableListOf<ApplicationType>()
                var position = -1
                for (type in config.ticket.applicationTypes) {
                    position += 1
                    listOfTypes.add(ApplicationType(position, type.roleID, "/", "/", "/", false, event.user.id, 0))
                }

                applicationTypeMap[event.user.id] = listOfTypes

                for (type in config.ticket.applicationTypes) {
                    ApplicationManager(config, translation).changeApplicationType(event.user.id, type.roleID, initializer = event.user.id, state = false, member = 0)
                }

                ApplicationMessageHandler(config, translation).sendApplicationMessage(event.jda, event.user.id, event.jda.getTextChannelById(config.ticket.settings.applicationMessageChannel)!!, false)
                val embed = Embed {
                    color = 0xE74D3C
                    title = ColorTool().useCustomColorCodes(translation.application.embedApplicationDeactivatedTitle)
                    description = ColorTool().useCustomColorCodes(translation.application.embedApplicationDeactivatedBody)
                }
                event.reply_("", listOf(embed)).setEphemeral(true).queue()
            } else {
                logger.warn("Could not deactivate application phase, add channel id in the config to fix")
            }
        }

        if (event.button.id?.startsWith("application_open") == true) {
            val embed = Embed {
                title = ColorTool().useCustomColorCodes(translation.support.embedApplicationPositionTitle)
                description = ColorTool().useCustomColorCodes(translation.support.embedApplicationPositionBody)
            }

            event.reply_("", listOf(embed)).addActionRow(
                StringSelectMenu.create("application_position").also {
                    for(type in config.ticket.applicationTypes) {
                        it.addOption(type.name, type.roleID, type.description, Emoji.fromFormatted(type.emoji))
                    }
                }.build()
            ).setEphemeral(true).queue()
        }
    }

    private fun nullCheck(): Boolean {
        if (applicationTypeMap.isNotEmpty()) return false
        event.message.delete().queue()
        event.reply_("This message has expired, please execute the command again").setEphemeral(true).queue()

        return true
    }
}