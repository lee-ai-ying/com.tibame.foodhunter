package com.tibame.foodhunter.sharon.data

import java.util.Date


data class Note(
    val type: CardContentType,
    val title: String,
    val noteId: Int,
    val selectedDate: Date = Date(),
    val date: String,
    val day: String,
    val content: String,
    val imageResId: Int? = null, // 可選欄位
    val restaurantName: String? = null, // 可選欄位
    val memberId: Int
)

data class Group(
    val type: CardContentType,
    val groupName: String, // fk group_id -> restaurant_name
    val restaurantName: String? = null, // fk restaurant_id -> restaurant_name
    val restaurantAddress: String? = null, // fk restaurant_id -> address
    val isPublic: Int, // fk group_id -> is_public
    val groupDate: Date = Date(), // fk group_id -> time
    val memberId: Int //  member_id
)

// 定義內容類型的枚舉
enum class CardContentType {
    GROUP,  // 揪團
    NOTE    // 筆記
}
