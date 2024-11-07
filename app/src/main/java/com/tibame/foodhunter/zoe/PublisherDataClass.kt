package com.tibame.foodhunter.zoe

import androidx.compose.ui.graphics.ImageBitmap
import com.tibame.foodhunter.R

data class Comment(
    val id: Int,
    val commenter: Commenter,
    val content: String,
    val timestamp: String
)

data class Commenter(
    val id: Int,
    val name: String,
    val avatarImage: Int = R.drawable.user1, // 預設頭像資源 ID
    val avatarBitmap: ImageBitmap? = null // 實際頭像資料
)

data class Publisher(
    val id: Int,
    val name: String,
    val avatarImage: Int = R.drawable.user1,  // 預設圖片資源 ID
    val avatarBitmap: ImageBitmap? = null,    // 實際頭像資料
    val followers: List<Publisher> = emptyList(), // 追蹤者列表
    val following: List<Publisher> = emptyList()  // 被追蹤者列表
)