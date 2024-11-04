package com.tibame.foodhunter.zoe
data class Comment(
    val id: Int,
    val commenter: Commenter,
    val content: String,
    val timestamp: String
)

data class Commenter(
    val id: Int,
    val name: String,
    val avatarImage: Int
)
data class Publisher(
    val id: Int,
    val name: String,
    val avatarImage: Int, // 圖片資源ID

    val followers: List<Publisher> = emptyList(), // 追蹤者列表
    val following: List<Publisher> = emptyList()  // 被追蹤者列表
)