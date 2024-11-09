package com.tibame.foodhunter.wei

import androidx.compose.ui.graphics.ImageBitmap


data class Reply(
    val id: Int,
    val replier: Replier, // 回覆者
    val content: String,
    val timestamp: String,
    )

data class Replier(
    val id: Int,
    val name: String,
    val avatarImage: ImageBitmap? = null,
)
