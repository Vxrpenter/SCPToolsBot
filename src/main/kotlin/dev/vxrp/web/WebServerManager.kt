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

package dev.vxrp.web

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.api.discord.Discord
import dev.vxrp.api.discord.data.DiscordConnection
import dev.vxrp.api.discord.data.DiscordUser
import dev.vxrp.bot.verify.VerifyMessageHandler
import dev.vxrp.configuration.data.Config
import dev.vxrp.configuration.data.Translation
import dev.vxrp.database.tables.database.UserTable
import dev.vxrp.util.coroutines.webServerScope
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import java.time.LocalDate

class WebServerManager(val api: JDA, val config: Config, val translation: Translation) {
    private val logger = LoggerFactory.getLogger(WebServerManager::class.java)

    init {
        webServerScope.launch {
            startWebServer()
        }
    }

    private fun startWebServer() {
        logger.info("Starting up webserver")

        embeddedServer(Netty, config.settings.webserver.port) {
            routing {
                get(config.settings.webserver.redirectUri) {
                    call.respondText("You were verified successfully, you can close this webpage now.")
                    val authorizationCode = call.request.queryParameters["code"]

                    if (authorizationCode != null) {
                        val tokenResponse = Discord().getAccessToken(
                            clientId = api.selfUser.id,
                            clientSecret = config.settings.clientSecret,
                            authorizationCode =  authorizationCode,
                            uri = config.settings.webserver.uri)
                        val user = Discord().getUser(tokenResponse)
                        val connections = Discord().getConnections(tokenResponse)

                        writeToDatabase(user, connections)
                    }
                }
            }
        }.start(wait = true)
    }

    private suspend fun writeToDatabase(user: DiscordUser, connections: List<DiscordConnection>) {
        for (connection in connections) {
            if (connection.type != "steam") continue

            logger.info("Received connection data from user: ${user.id}")
            UserTable().addToDatabase(user.id, LocalDate.now().toString(), connection.id)

            val currentUser = api.retrieveUserById(user.id).await()
            VerifyMessageHandler(api, config, translation).sendVerificationMessage(currentUser)
        }
    }
}