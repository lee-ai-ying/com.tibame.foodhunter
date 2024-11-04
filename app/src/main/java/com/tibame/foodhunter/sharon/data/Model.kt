package com.tibame.foodhunter.sharon.data



data class Note(
    val noteId: Int,
    val type: CardContentType,
    val date: String,
    val day: String,
    val title: String,
    val noteContent: String,
    val imageResId: Int? = null, // 可選欄位
    val restaurantName: String? = null  // 可選欄位
)

// 定義內容類型的枚舉
enum class CardContentType {
    GROUP,  // 揪團
    NOTE    // 筆記
}
