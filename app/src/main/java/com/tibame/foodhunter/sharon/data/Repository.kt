package com.tibame.foodhunter.sharon.data

import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.card.CardContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 定義一個單例物件 NoteRepository，用於管理筆記相關的數據
object NoteRepository {
    // 創建一個私有的可變狀態流，用於存儲筆記列表，初始值為空列表
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())

    // 創建一個公開的唯讀狀態流，供外部訪問筆記列表數據
    val noteList: StateFlow<List<Note>> = _noteList.asStateFlow()

    // 初始化代碼塊，當 NoteRepository 首次被訪問時執行
    init {
        // 初始化筆記列表數據，並賦值給 _noteList
        _noteList.value = listOf(
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
    }
}