package com.tibame.foodhunter.zoe

data class Comment(
    val id: String,
    val commenter: Commenter, // 留言者
    val content: String,
    val timestamp: String
)

data class Commenter(
    val id: String,
    val name: String,
    val avatarImage: Int // 圖片資源ID
)
data class Publisher(
    val id: String,
    val name: String,
    val avatarImage: Int, // 圖片資源ID
    val joinDate: String,
    val followers: List<Publisher> = emptyList(), // 追蹤者列表
    val following: List<Publisher> = emptyList()  // 被追蹤者列表
)