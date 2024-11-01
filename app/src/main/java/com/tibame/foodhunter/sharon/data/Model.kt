package com.tibame.foodhunter.sharon.data

import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.card.CardContentType

//data class NoteData(
//    val noteId: Int,
//    val memberId: Int
//)

data class NoteData(
    val id: String,
    val type: CardContentType,
    val date: String,
    val day: String,
    val title: String,
    val noteContent: String,
    val imageResId: Int,
    val restaurantName: String? = null  // 可選欄位
)

// 假資料
val noteList = listOf(
    NoteData(
        id = "1",
        type = CardContentType.NOTE,
        date = "10/15",
        day = "星期二",
        title = "巷弄甜點店",
        noteContent = "隱藏在民生社區的法式甜點，檸檬塔酸甜適中...",
        imageResId = R.drawable.sushi_image_1,
        restaurantName = "法式甜點工作室"
    ),
    NoteData(
        id = "2",
        type = CardContentType.NOTE,
        date = "10/16",
        day = "星期三",
        title = "老饕牛肉麵",
        noteContent = "湯頭清甜不油膩，牛肉軟嫩入味，加上特製辣醬...",
        imageResId = R.drawable.sushi_image_1,
        restaurantName = "阿明牛肉麵"
    )
)