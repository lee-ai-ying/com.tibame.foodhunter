package com.tibame.foodhunter.a871208s


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

const val serverUrl = "http://10.2.17.40:8080/com.tibame.foodhunter_server"

private const val myTag = "tag_Common"

suspend fun httpPost(
    url: String,
    dataOut: String
): String {
    var dataIn = ""
    withContext(Dispatchers.IO) {
        (URL(url).openConnection() as? HttpURLConnection)?.run {
            Log.d("", "openConnection()")
            doInput = true // allow inputs
            doOutput = true // allow outputs
            // 將送出請求內容分段傳輸，設定0代表使用預設大小
            setChunkedStreamingMode(0)
            useCaches = false
            // 一定要大寫
            requestMethod = "POST"
            setRequestProperty("content-type", "application/json")
            setRequestProperty("charset", "utf-8")
            Log.d(myTag, "dataOut: $dataOut")
            outputStream.bufferedWriter().use { it.write(dataOut) }
            if (responseCode == 200) {
                inputStream.bufferedReader().useLines { lines ->
                    dataIn = lines.fold("") { text, line -> "$text$line" }
                    Log.d(myTag, "dataIn: $dataIn")
                }
            }
        }
    }
    return dataIn
}