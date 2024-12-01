package dev.vxrp.api.sla.scplist

import dev.vxrp.scplist.data.ScpListServers
import dev.vxrp.scplist.data.Server
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

@Serializable
private data class Info(
    val search: String,
    val countryFilter: List<String>,
    val hideEmptyServer: Boolean,
    val hideFullServer: Boolean,
    val friendlyName: Boolean,
    val whitelist: Boolean,
    val modded: Boolean,
    val sort: String)

/**
 * ### ScpList Api Kotlin
 *
 * A small tool for accessing the `scplist.kr` api.
 *
 * @author Vxrpenter
 * @since Sl Version `13.5.1`
 */
class ScpList(readTimeout: Long = 60, writeTimeout: Long = 60) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .writeTimeout(writeTimeout, TimeUnit.SECONDS)
        .build()

    /**
     * Endpoint for getting information about al servers
     *
     * Endpoint: `/api/servers`
     *
     * @param search the search quote
     * @param countryFilter what countries to filter
     * @param hideEmptyServer hide empty servers?
     * @param hideFullServer hide full servers?
     * @param friendlyFire show friendly fire servers
     * @param whitelist show whitelisted servers?
     * @param modded show modded servers?
     * @param sort how to sort?
     *
     * @return the ScpListServers
     */
    fun serverPost(search: String, countryFilter: List<String>, hideEmptyServer: Boolean = true, hideFullServer: Boolean = true, friendlyFire: Boolean = true, whitelist: Boolean = true, modded: Boolean = true, sort: String = "PLAYERS_DESC"): ScpListServers? {
        val data = Info(search, countryFilter, hideEmptyServer, hideFullServer, friendlyFire, whitelist, modded, sort)

        val request = Request.Builder()
            .url("https://api.scplist.kr/api/servers")
            .post(Json.encodeToString(data).toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null

            return Json.decodeFromString<ScpListServers>(response.body!!.string())
        }
    }

    /**
     * Endpoint for getting a specific server
     *
     * Endpoint: `/api/servers/{serverId}`
     *
     * @param serverId id of the servers
     *
     * @return the Server
     */
    fun serverGet(serverId: String): Server? {

        val request = Request.Builder()
            .url("https://api.scplist.kr/api/servers/$serverId")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null

            return Json.decodeFromString<Server>(response.body!!.string())
        }
    }
}