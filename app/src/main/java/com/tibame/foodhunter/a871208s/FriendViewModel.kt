package com.tibame.foodhunter.a871208s

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ai_ying.GroupRepository
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FriendViewModel: ViewModel() {
    // MutableStateFlow用來監控指定資料狀態，當資料一改變即可通知對應畫面更新
    // MutableStateFlow常與ViewModel搭配，可以讓UI元件在生命週期期間作出適當更新
    private val _FriendState = MutableStateFlow(emptyList<Friend>())
    private val _SFriendState = MutableStateFlow(emptyList<Friend>())
    val FriendState: StateFlow<List<Friend>> = _FriendState.asStateFlow()
    val SFriendState: StateFlow<List<Friend>> = _SFriendState.asStateFlow()
    // 一開始就呼叫fetchBooks()取得測試資料以更新_bookState內容
    init {
        _FriendState.update { fetchFriends() }
        _SFriendState.update { fetchSFriends() }
    }

    /** 新增一本書到List並更新_bookState內容 */
    fun addItem(item: Friend) {
        _FriendState.update {
            val books = it.toMutableList()
            books.add(item)
            books
        }
    }

    /** 移除一本書並更新_bookState內容 */
    fun removeItem(item: Friend) {
        _FriendState.update {
            val books = it.toMutableList()
            books.remove(item)
            books
        }
    }
    fun fetchFriends(): List<Friend> {
        return listOf(
            Friend("0001", "Ivy", R.drawable.image),
            Friend("0002", "Mary", R.drawable.account_circle),
            Friend("0003", "Sue", R.drawable.account_circle),
            Friend("0004", "Sue", R.drawable.account_circle),
            Friend("0005", "Ivy2", R.drawable.image),
            Friend("0006", "Mary2", R.drawable.account_circle),
            Friend("0007", "Sue2", R.drawable.account_circle),
            Friend("0008", "Sue2", R.drawable.account_circle),
        )
    }

    fun fetchSFriends(): List<Friend> {
        return listOf(
            Friend("0009", "Ivy3", R.drawable.image),
            Friend("0010", "Mary3", R.drawable.account_circle),
            Friend("0011", "Sue3", R.drawable.account_circle),
            Friend("0012", "Sue3", R.drawable.account_circle),
            Friend("0013", "Ivy3", R.drawable.image),
            Friend("0014", "Mary3", R.drawable.account_circle),
            Friend("0015", "Sue3", R.drawable.account_circle),
            Friend("0016", "Sue3", R.drawable.account_circle),
        )
    }


    suspend fun friendAdd(
        username: String,
        friend: String,

    ): Boolean {
        try {
            // server URL
            val url = "$serverUrl/member/friendAdd"
            val gson = Gson()
            val jsonObject = JsonObject()

            // 將註冊資料轉成 JSON
            jsonObject.addProperty("username", username)
            jsonObject.addProperty("friend", friend)


            // 發出 POST 請求，取得註冊結果
            val result = CommonPost(url, jsonObject.toString())
            val responseJson = gson.fromJson(result, JsonObject::class.java)
            Log.e("Response", result) // 输出响应结果
            // 根據響應中的 logged 屬性來判斷是否註冊成功
            return  responseJson.get("Add").asBoolean
        } catch (e: Exception) {
            return false
        }
    }
}