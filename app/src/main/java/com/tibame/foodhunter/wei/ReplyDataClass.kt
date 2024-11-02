package com.tibame.foodhunter.wei


data class Reply(
    val id: String,
    val replier: Replier, // 回覆者
    val content: String,
    val timestamp: String,)

data class Replier(
    val id: String,
    val name: String,
    val avatarImage: Int // 圖片資源ID
)
data class Reviewer(
    val id: String,
    val name: String,
    val avatarImage: Int, // 圖片資源ID
    val followers: Int = 0, // 追蹤者人數
    val following: Int = 0  // 追蹤中人數
)