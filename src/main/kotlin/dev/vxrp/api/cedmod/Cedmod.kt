package dev.vxrp.api.cedmod

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class Cedmod(private val instanceUrl: String, private val apiKey: String, private val readTimeout: Long, private val writeTimeout: Long) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .writeTimeout(writeTimeout, TimeUnit.SECONDS)
        .build()

    fun retrievePlayStats(steamId: String, timeframe: String) {
        val request = Request.Builder()
            .url("$instanceUrl/Api/Player/Query?q=$steamId@steam&max=10&page=0&staffOnly=false&create=false&sortLabel=id_field&activityMin=$timeframe&basicStats=true&moderationData=false")
            .header("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).execute().use { response ->
        }
    }
}