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

package dev.vxrp.bot.main.commands

import dev.minn.jda.ktx.interactions.commands.choice
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.commands.restrict
import dev.minn.jda.ktx.interactions.commands.slash
import dev.minn.jda.ktx.interactions.commands.subcommand
import dev.minn.jda.ktx.interactions.commands.updateCommands
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
object Commands {
    fun initializeCommands(api: JDA) = api.updateCommands {
        slash("template", "Command for pasting certain templates") {
            restrict(guild = true, Permission.ADMINISTRATOR)
            option<String>("template", "What template are you referring to", true) {
                choice("support", "support")
                choice("verify", "verify")
                choice("notice of departure", "notice of departure")
                choice("regulars", "regulars")
            }
        }

        slash("verify", "Command used for member verification") {
            restrict(guild = true, Permission.ADMINISTRATOR)
        }

        slash("notice_of_departure", "View information on notices and change data") {
            restrict(guild = true, Permission.ADMINISTRATOR)
            subcommand("view", "View a notice of departure") {
                option<User>("user", "User to query from", true)
            }
            subcommand("revoke", "Revoke a notice of departure") {
                option<User>("user", "User to revoke from", true)
            }
        }

        slash("regulars", "Use for configuring regulars and modifying saved information") {
            restrict(guild = true, Permission.ADMINISTRATOR)
            subcommand("view", "View a regular user") {
                option<User>("user", "User to query from", true)
            }
            subcommand("remove", "Remove a regular") {
                option<User>("user", "User to remove from", true)
            }
        }

        slash("application", "Command for managing applications") {
            restrict(guild = true, Permission.ADMINISTRATOR)
            option<String>("state", "Set the application state", true) {
                choice("activate", "activate")
                choice("deactivate", "deactivate")
            }
        }
    }
}