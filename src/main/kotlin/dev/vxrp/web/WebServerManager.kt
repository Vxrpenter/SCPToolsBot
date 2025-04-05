
package dev.vxrp.web

import dev.minn.jda.ktx.coroutines.await
import dev.vxrp.api.discord.Discord
import dev.vxrp.api.discord.enums.DiscordConnection
import dev.vxrp.api.discord.enums.DiscordTokenResponse
import dev.vxrp.api.discord.enums.DiscordUser
import dev.vxrp.bot.verify.VerifyMessageHandler
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.UserTable
import dev.vxrp.util.webServerScope
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
                            uri = "${config.settings.webserver.uri}:${config.settings.webserver.port}${config.settings.webserver.redirectUri}")
                        val user = Discord().getUser(tokenResponse)
                        val connections = Discord().getConnections(tokenResponse)

                        writeToDatabase(tokenResponse, user, connections)
                    }
                }
            }
        }.start(wait = true)
    }

    private fun writeToDatabase(tokenResponse: DiscordTokenResponse, user: DiscordUser, connections: List<DiscordConnection>) {
        for (connection in connections) {
            if (connection.type != "steam") continue

            logger.info("Received connection data and refresh token from user: ${user.id}")
            UserTable().addToDatabase(user.id, LocalDate.now().toString(), connection.id, tokenResponse.refreshToken)

            VerifyMessageHandler(api, config, translation).sendVerificationMessage(user.username)
        }
    }
}