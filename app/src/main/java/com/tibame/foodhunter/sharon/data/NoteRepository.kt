package com.tibame.foodhunter.sharon.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 筆記資料存取層
 * 專注於與後端API的溝通和資料轉換
 */
class NoteRepository private constructor() {
    companion object {
        // 伺服器URL，注意要改成您的URL
        private const val SERVER_URL = "http://10.0.2.2:8080/foodhunter_server"
        // 單例模式實例
        val instance = NoteRepository()
    }

    // 用於存放API回傳的筆記列表
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val gson = Gson()

    /**
     * 從後端獲取所有筆記
     * 成功時更新 _notes 狀態流
     * 失敗時設置空列表
     */
    suspend fun getNotes() {
        try {
            val url = "$SERVER_URL/SelectAllNotesController"
            val result = httpGet(url)

            // 解析 API 回應
            val type = object : TypeToken<List<NoteApiResponse>>() {}.type
            val noteResponses: List<NoteApiResponse> = gson.fromJson(result, type)

            // 轉換並更新 StateFlow
            _notes.value = noteResponses.map { it.toNote() }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error getting notes: ${e.message}")
            _notes.value = emptyList()
        }
    }

    /**
     * 根據ID取得單一筆記
     * @param noteId 筆記ID
     * @return 筆記資料，若查無資料或發生錯誤則回傳null
     */
    suspend fun getNoteById(noteId: Int): Note? {
        val url = "$SERVER_URL/SelectNoteController?note_id=$noteId"
        Log.d("NoteRepository", "開始從 $url 獲取筆記")

        return try {
            // 發送GET請求取得回應
            val result = httpGet(url)
            Log.d("NoteRepository", "API回應: $result")

            // 使用JsonObject解析回應，因為可能包含錯誤訊息
            val response = gson.fromJson(result, JsonObject::class.java)

            // 檢查回應是否包含錯誤訊息
            if (response.has("error") || response.has("NotFind")) {
                Log.d("NoteRepository", "回應含錯誤: $response")
                null
            } else {
                // 轉換成Note物件
                Note(
                    noteId = response.get("note_id").asInt,
                    title = response.get("title").asString,
                    noteContent = response.get("content").asString,
                    type = CardContentType.NOTE,  // 目前固定為NOTE類型
                    date = formatDate(response.get("selected_date").asString),
                    day = formatDay(response.get("selected_date").asString),
                    restaurantName = null,  // 未來可能需要額外API獲取餐廳資訊
                    imageResId = null
                ).also {
                    Log.d("NoteRepository", "成功建立Note物件: $it")
                }
            }
        } catch (e: Exception) {
            // 紀錄錯誤並返回null
            Log.e("NoteRepository", "獲取筆記時發生錯誤: ${e.message}")
            e.printStackTrace()
            null
        }
    }


    /**
     * API回應的資料結構，用於匹配後端回傳的JSON格式
     * 主要用於資料傳輸和解析
     */
    data class NoteApiResponse(
        val note_id: Int,        // 後端回傳ID是數字型態
        val title: String,
        val content: String,
        val restaurant_id: Int,  // 後端回傳餐廳ID
        val member_id: Int,      // 後端回傳會員ID
        val selected_date: String // 後端回傳的日期格式 "2024-10-14 00:00:00.0"
    )

    // API 資料轉換方法通常使用 "to" 前綴
    private fun NoteApiResponse.toNote(): Note {
        return Note(
            noteId = note_id,
            title = title,
            noteContent = content,
            type = CardContentType.NOTE,
            date = formatDate(selected_date),
            day = formatDay(selected_date),
            restaurantName = null,
            imageResId = null
        )
    }

    /**
     * 格式化日期 (MM/dd)
     */
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date)
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error formatting date: ${e.message}")
            ""
        }
    }

    /**
     * 格式化星期幾
     */
    private fun formatDay(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE", Locale.CHINESE)
        return try {
            val date = inputFormat.parse(dateString)
            "星期" + outputFormat.format(date).substring(2, 3)
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error formatting day: ${e.message}")
            ""
        }
    }
}