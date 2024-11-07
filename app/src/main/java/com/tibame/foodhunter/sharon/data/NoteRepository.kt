package com.tibame.foodhunter.sharon.data

import android.icu.util.LocaleData
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

/**
 * 筆記資料存取層
 * 專注於與後端API的溝通和資料轉換
 */
class NoteRepository private constructor() {
    companion object {
        private const val TAG = "NoteRepository"
        // 使用團隊統一的 serverUrl
        private const val BASE_URL = serverUrl
        private const val API_PATH = "/api/note"
        val instance = NoteRepository()
    }

    // StateFlow 用於觀察筆記列表的變化
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val gson = Gson()

    /**
     * 取得所有筆記
     * 使用團隊統一的 CommonPost 方法
     */
    suspend fun getNotes() {
        try {
            // 組合完整 URL
            val url = "$BASE_URL$API_PATH/getAllNotes"

            // 呼叫共用的 POST 方法
            // 因為 getAllNotes 不需要參數，傳空的 JsonObject
            val response = CommonPost(url, JsonObject().toString())
            Log.d("NoteRepo", "收到回應: $response")

            // 檢查是否有回應
            if (response.isNotEmpty()) {
                // 解析回應
                val jsonResponse = gson.fromJson(response, JsonObject::class.java)

                // 檢查是否有錯誤
                if (jsonResponse.has("error")) {
                    Log.e(TAG, "API返回錯誤: ${jsonResponse.get("error").asString}")
                    _notes.value = emptyList()
                    return
                }

                // 取得筆記陣列
                val notesArray = jsonResponse.getAsJsonArray("notes")
                val notesList = notesArray.map { noteJson ->
                    val noteObj = noteJson.asJsonObject
                    NoteApiResponse(
                        note_id = noteObj.get("note_id").asInt,
                        title = noteObj.get("title").asString,
                        content = noteObj.get("content").asString,
                        restaurant_id = noteObj.get("restaurant_id").asInt,
                        member_id = noteObj.get("member_id").asInt,
                        selected_date = noteObj.get("selected_date").asString
                    ).toNote()
                }


                // 更新 StateFlow
                _notes.value = notesList
                Log.d("NoteRepo", "轉換後的資料: $notesList")


                // 記錄總筆數
                val total = jsonResponse.get("total").asInt
                Log.d(TAG, "成功取得筆記列表，總筆數: $total")
            } else {
                Log.e(TAG, "API 無回應")
                _notes.value = emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "獲取筆記列表時發生錯誤", e)
            _notes.value = emptyList()
        }
    }

    /**
     * 根據ID取得單一筆記
     */
    suspend fun getNoteById(noteId: Int): Note? {
        val url = "$BASE_URL$API_PATH/getNoteById"

        try {
            // 準備請求參數
            val requestBody = JsonObject().apply {
                addProperty("note_id", noteId)
            }

            // 發送請求
            val response = CommonPost(url, requestBody.toString())
            Log.d(TAG, "API回應: $response")

            if (response.isNotEmpty()) {
                // 解析回應
                val jsonResponse = gson.fromJson(response, JsonObject::class.java)

                // 檢查是否有錯誤
                if (jsonResponse.has("error") || jsonResponse.has("NotFind")) {
                    Log.d(TAG, "回應含錯誤: $jsonResponse")
                    return null
                }

                // 轉換成Note物件
                return Note(
                    noteId = jsonResponse.get("note_id").asInt,
                    title = jsonResponse.get("title").asString,
                    noteContent = jsonResponse.get("content").asString,
                    type = CardContentType.NOTE,
                    date = formatDate(jsonResponse.get("selected_date").asString),
                    day = formatDay(jsonResponse.get("selected_date").asString),
                    restaurantName = null,
                    imageResId = null
                ).also {
                    Log.d(TAG, "成功建立Note物件: $it")
                }
            }
            return null
        } catch (e: Exception) {
            Log.e(TAG, "獲取筆記時發生錯誤", e)
            return null
        }
    }

    /**
     * 新增筆記
     * @return 新增成功返回筆記ID，失敗返回null
     */
    suspend fun addNote(title: String, content: String, type: CardContentType, restaurantName: String? = null): Int? {
        val url = "$BASE_URL$API_PATH/addNote"

        try {
            // 準備請求參數
            val requestBody = JsonObject().apply {
                addProperty("title", title)
                addProperty("content", content)
                addProperty("type", type.name)  // 轉換為字串
                // 暫時固定傳入的值，之後可以改為從登入狀態獲取
                addProperty("member_id", 1)
                addProperty("restaurant_id", restaurantName)  // 暫時不處理餐廳關聯
                // 使用當前日期
                addProperty("selected_date", LocalDate.now().toString())
            }

            // 發送請求
            val response = CommonPost(url, requestBody.toString())
            Log.d(TAG, "新增筆記API回應: $response")

            if (response.isNotEmpty()) {
                val jsonResponse = gson.fromJson(response, JsonObject::class.java)

                // 檢查是否成功
                if (jsonResponse.has("error")) {
                    Log.e(TAG, "新增筆記失敗: ${jsonResponse.get("error").asString}")
                    return null
                }

                // 取得新增的筆記ID
                val newNoteId = jsonResponse.get("note_id").asInt

                // 重新取得筆記列表
                getNotes()

                return newNoteId
            }
            return null
        } catch (e: Exception) {
            Log.e(TAG, "新增筆記時發生錯誤", e)
            return null
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
        try {
            val date = inputFormat.parse(dateString)
                ?: throw IllegalArgumentException("無法解析日期: $dateString")
            return outputFormat.format(date)
        } catch (e: Exception) {
            Log.e(TAG, "日期格式化錯誤: $dateString", e)
            throw IllegalArgumentException("日期格式錯誤: $dateString", e)
        }
    }

    /**
     * 格式化星期幾
     */
    private fun formatDay(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE", Locale.CHINESE)
         try {
            val date = inputFormat.parse(dateString)
                ?: throw IllegalArgumentException("無法解析日期: $dateString")

             return "星期" + outputFormat.format(date).substring(2, 3)
        } catch (e: Exception) {
             Log.e(TAG, "日期格式化錯誤: $dateString", e)
             throw IllegalArgumentException("日期格式錯誤: $dateString", e)
        }
    }
}