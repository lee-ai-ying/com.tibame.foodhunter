package com.tibame.foodhunter.sharon.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


suspend fun httpGet(url: String): String {
    var response = ""
    withContext(Dispatchers.IO) {
        (URL(url).openConnection() as? HttpURLConnection)?.run {
            Log.d("Http", "openConnection()")
            doInput = true
            useCaches = false
            requestMethod = "GET"
            setRequestProperty("content-type", "application/json")
            setRequestProperty("charset", "utf-8")

            if (responseCode == 200) {
                response = inputStream.bufferedReader().use { it.readText() }
                Log.d("response", response)
            } else {
                println("false")
            }
        }
    }
    return response
}