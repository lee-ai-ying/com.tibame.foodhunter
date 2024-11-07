package com.tibame.foodhunter.sharon.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
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

                // 檢查 JSON 解析
                val notesArray = jsonResponse.getAsJsonArray("notes")
                Log.d(TAG, "解析到的筆記陣列: $notesArray")  // 看JSON陣列


                val notesList = notesArray.map { noteJson ->
                    val noteObj = noteJson.asJsonObject
                    Log.d(TAG, "單筆筆記JSON: $noteObj")
                    // 看單筆資料

                    // 取得筆記陣列
                    val apiResponse = NoteApiResponse(
                        note_id = noteObj.get("note_id").asInt,
                        title = noteObj.get("title").asString,
                        content = noteObj.get("content").asString,  // 檢查 content
                        restaurant_id = noteObj.get("restaurant_id").asInt,
                        member_id = noteObj.get("member_id").asInt,
                        selected_date = noteObj.get("selected_date").asString
                    )

                    Log.d(TAG, "轉換成 ApiResponse: $apiResponse")  // 看轉換結果
                    apiResponse.toNote()

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
            e.printStackTrace()
            _notes.value = emptyList()
        }
    }

    /**
     * 根據ID取得單一筆記
     */
    suspend fun getNoteById1(noteId: Int): Note? {
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
                    content = jsonResponse.get("content").asString,
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
     * 根據ID取得單一筆記 (本地測試版本)
     */
    suspend fun getNoteById(noteId: Int): Note? {
        // 先嘗試從本地列表查找
        val localNote = _notes.value.find { it.noteId == noteId }
        if (localNote != null) {
            Log.d(TAG, "從本地找到筆記: $localNote")
            return localNote
        }

        // 如果本地找不到，且不想使用API，直接返回null
        Log.d(TAG, "本地找不到筆記ID: $noteId")
        return null

        // 先註解掉 API 呼叫的部分
        /*
        val url = "$BASE_URL$API_PATH/getNoteById"
        try {
            // API 呼叫的程式碼...
        } catch (e: Exception) {
            Log.e(TAG, "獲取筆記時發生錯誤", e)
            return null
        }
        */
    }

    /**
     * 測試用：本地新增筆記
     * 當後端API還未完成時使用
     */
    suspend fun addNoteLocal(
        title: String,
        content: String,
        type: CardContentType,
        restaurantName: String? = null,
    ): Int? {
        try {
            // 生成臨時 ID
            val newNoteId = System.currentTimeMillis().toInt()

            // 取得當前日期並格式化
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
                .format(java.util.Date())

            // 創建新筆記
            val newNote = Note(
                noteId = newNoteId,
                title = title,
                content = content,
                type = type,
                date = formatDate(formattedDate),  // 使用正確格式的日期字串
                day = formatDay(formattedDate),    // 使用正確格式的日期字串
                restaurantName = restaurantName,
                imageResId = null
            )

            // 添加到現有列表
            val currentList = _notes.value.toMutableList()
            currentList.add(0, newNote)  // 加到最前面
            _notes.value = currentList

            Log.d(TAG, "本地新增筆記成功: $newNote")
            return newNoteId

        } catch (e: Exception) {
            Log.e(TAG, "本地新增筆記失敗", e)
            throw e
        }
    }

    // 修改 addNote 方法，暫時使用本地測試版本
    suspend fun addNote(
        title: String,
        content: String,
        type: CardContentType,
        restaurantName: String? = null,
    ): Int? {
        // 暫時使用本地版本測試
        return try {
            addNoteLocal(title, content, type, restaurantName)
        } catch (e: Exception) {
            Log.e(TAG, "新增筆記失敗", e)
            null
        }
    }

    /**
     * 新增筆記
     * @return 新增成功返回筆記ID，失敗返回null
     */
    suspend fun addNote1(
        title: String,
        content: String,
        type: CardContentType,
        restaurantName: String? = null,
    ): Int? {
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
        val selected_date: String, // 後端回傳的日期格式 "2024-10-14 00:00:00.0"
    )

    // API 資料轉換方法通常使用 "to" 前綴
    private fun NoteApiResponse.toNote(): Note {
        return Note(
            noteId = note_id,
            title = title,
            content = content,
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