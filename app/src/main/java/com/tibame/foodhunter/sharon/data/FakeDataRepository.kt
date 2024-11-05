package com.tibame.foodhunter.sharon.data.repository

import com.google.gson.Gson
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Note
import kotlinx.coroutines.flow.MutableStateFlow

object NoteRepository {
    private val gson = Gson()
    private const val isMockMode = true // 控制是否使用假資料
    private const val serverUrl = "http://10.0.2.2:8080/com.tibame.foodhunter_server"

    // 假資料
    private val mockNotes = listOf(
        Note(
            noteId = 1,
            type = CardContentType.NOTE,
            date = "10/15",
            day = "星期二",
            title = "巷弄甜點店",
            noteContent = "隱藏在民生社區的法式甜點，檸檬塔酸甜適中...",
            imageResId = R.drawable.sushi_image_1,
            restaurantName = "法式甜點工作室"
        ),
        Note(
            noteId = 2,
            type = CardContentType.NOTE,
            date = "10/16",
            day = "星期三",
            title = "老饕牛肉麵",
            noteContent = "湯頭清甜不油膩，牛肉軟嫩入味，加上特製辣醬...",
            imageResId = R.drawable.sushi_image_1,
            restaurantName = "阿明牛肉麵"
        )
    )

    // 用於假資料或 API 資料的 MutableStateFlow
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())


}
