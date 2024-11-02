package com.tibame.foodhunter.sharon.data

import com.tibame.foodhunter.sharon.components.card.CardContentType


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

