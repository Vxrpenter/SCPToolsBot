/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.api.discord

import dev.vxrp.api.discord.data.DiscordConnection
import dev.vxrp.api.discord.data.DiscordTokenResponse
import dev.vxrp.api.discord.data.DiscordUser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

class Discord {
    private val logger = LoggerFactory.getLogger(Discord::class.java)
    private val jsonDecoder = Json { ignoreUnknownKeys = true }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = HttpClient(CIO) {
        install(ClientContentNegotiation) {
            json(json)
            formData()
        }
    }

    suspend fun getAccessToken(clientId: String, clientSecret: String, authorizationCode: String, uri: String): DiscordTokenResponse {
        val tokenCall = client.post("https://discord.com/api/oauth2/token") {
            setBody(FormDataContent(Parameters.build {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("grant_type", "authorization_code")
                append("code", authorizationCode)
                append("redirect_uri", uri)
            }))
        }

        if (!tokenCall.status.isSuccess()) logger.error("Failed to retrieve access token from Discord OAuth Api")

        return jsonDecoder.decodeFromString<DiscordTokenResponse>(tokenCall.bodyAsText())
    }

    suspend fun getUser(tokenResponse: DiscordTokenResponse): DiscordUser {
        val userCall = client.get("https://discord.com/api/users/@me") {
            header("Authorization", "Bearer ${tokenResponse.accessToken}")
        }

        if (!userCall.status.isSuccess()) logger.error("Failed to retrieve user data from Discord OAuth Api")

        return jsonDecoder.decodeFromString<DiscordUser>(userCall.bodyAsText())
    }

    suspend fun getConnections(tokenResponse: DiscordTokenResponse): List<DiscordConnection> {
        val connectionCall = client.get("https://discord.com/api/users/@me/connections") {
            header("Authorization", "Bearer ${tokenResponse.accessToken}")
        }

        if (!connectionCall.status.isSuccess()) logger.error("Failed to retrieve connection data from Discord OAuth Api")

        return jsonDecoder.decodeFromString<List<DiscordConnection>>(connectionCall.bodyAsText())
    }
}