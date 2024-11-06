package com.tibame.foodhunter.a871208s

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.andysearch.Restaurant
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import java.io.InputStream

class UserViewModel: ViewModel() {
    // 登入
    suspend fun login(username: String, password: String): Boolean {
        try {
            val url = "${serverUrl}/member/login"
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", username)
            jsonObject.addProperty("password", password)

            val result = CommonPost(url, jsonObject.toString())
            val responseJson = gson.fromJson(result, JsonObject::class.java)


            return responseJson.get("logged").asBoolean
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun register(
        username: String,
        password: String,
        nickname: String,
        email: String,
        phone:String,
        gender: String,
        birthday: String,
    ): Boolean {
        try {
            // server URL
            val url = "${serverUrl}/member/register"
            val gson = Gson()
            val jsonObject = JsonObject()

            // 將註冊資料轉成 JSON
            jsonObject.addProperty("username", username)
            jsonObject.addProperty("password", password)
            jsonObject.addProperty("nickname", nickname)
            jsonObject.addProperty("email", email)
            jsonObject.addProperty("phone", phone)
            jsonObject.addProperty("gender", gender)
            jsonObject.addProperty("birthday", birthday)

            // 發出 POST 請求，取得註冊結果
            val result = CommonPost(url, jsonObject.toString())
            val responseJson = gson.fromJson(result, JsonObject::class.java)

            // 根據響應中的 logged 屬性來判斷是否註冊成功
            return  responseJson.get("registered").asBoolean
        } catch (e: Exception) {
            return false
        }
    }

    // 獲取用戶資料
    suspend fun getUserInfo1(username: String): User? {
        return try {
            val url = "${serverUrl}/member/getInfo"
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", username)

            val result = CommonPost(url, jsonObject.toString())
            val userType = object : TypeToken<User>() {}.type
            Log.e("aaa",result)
            Log.e("aab",gson.fromJson(result, userType))
            return gson.fromJson(result, userType)

        } catch (e: Exception) {
            Log.e("UserViewModel", "Error fetching user info: ${e.message}")
            null
        }
    }



    suspend fun getUserInfo(username: String): User? {
        return try {
            val url = "${serverUrl}/member/getInfo"
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", username)

            val result = CommonPost(url, jsonObject.toString())
            Log.e("Response", result) // 输出响应结果

            // 使用 TypeToken 获取 User 的类型
            val userType = object : TypeToken<User>() {}.type
            val user = gson.fromJson<User>(result, userType) // 显式指定类型

            user
        } catch (e: Exception) {
            Log.e("UserViewModel", "获取用户信息时出错: ${e.message}")
            null
        }
    }

    // 用戶數據類
    data class User(
        val id: Int,  // 确保添加 id 字段
        val username: String,
        val password: String,
        val nickname: String,
        val email: String,
        val phone: String,
        val registrationdate: String,
        val profileimage: InputStream?,
        val gender: String,
        val birthday: String
    )

    var username = mutableStateOf("")

}



/** 上傳帳密與圖像 */
/*suspend fun uploadImageWithUser(
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
}*/
