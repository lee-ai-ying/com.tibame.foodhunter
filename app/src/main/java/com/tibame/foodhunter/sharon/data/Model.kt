package com.tibame.foodhunter.sharon.data

import java.time.LocalDate


data class Note(
    val noteId: Int,
    val type: CardContentType,
    val date: String,
    val day: String,
    val title: String,
    val content: String,
    val imageResId: Int? = null, // 可選欄位
    val restaurantName: String? = null  // 可選欄位
)

data class Group(
    val id: Int,
    val type: CardContentType,
    val title: String,
    val location: String,
    val date: LocalDate,
    val memberCount: Int,
    val memberId: Int
)

// 定義內容類型的枚舉
enum class CardContentType {
    GROUP,  // 揪團
    NOTE    // 筆記
}
