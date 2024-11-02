/*package com.tibame.foodhunter.a871208s

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.InputStream

class UserViewModel: ViewModel() {
    // 登入
    suspend fun login(uid: String, password: String): Boolean {
        // server URL
        val url = "${serverUrl}/LoginServlet"
        val gson = Gson()
        var jsonObject = JsonObject()

        // 將帳密轉成JSON
        jsonObject.addProperty("uid", uid)
        jsonObject.addProperty("password", password)

        // 發出POST請求，取得登入結果後回傳
        val result = httpPost(url, jsonObject.toString())
        jsonObject = gson.fromJson(result, jsonObject::class.java)
        return jsonObject.get("logged").asBoolean
    }

    /** 上傳帳密與圖像 */
    suspend fun uploadImageWithUser(
        uid: String,
        password: String,
        inputStream: InputStream?
    ): Boolean {
        var registered = false
        val url = "${serverUrl}/RegisterServlet"
        val client = HttpClient()
        val byteArray = inputStream?.readBytes()
            ?: throw IllegalStateException("Unable to read URI as ByteArray")

        val httpResponse = client.submitFormWithBinaryData(url = url, formData = formData {
            append("uid", uid) // Add user ID to the form data
            append("password", password) // Add user password to the form data
            // Add image as binary file part
            append("image", byteArray, Headers.build {
                append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                append(HttpHeaders.ContentDisposition, "filename=\"upload.jpg\"")
            })
        })
        // response狀態成功代表註冊成功
        if (httpResponse.status.isSuccess()) {
            registered = true
        }
        Log.d("tag_UserVM", httpResponse.bodyAsText())
        return registered
    }

}

 */