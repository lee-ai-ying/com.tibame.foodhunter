package com.tibame.foodhunter.wei


data class Reply(
    val id: Int,
    val replier: Replier, // 回覆者
    val content: String,
    val timestamp: String,)

data class Replier(
    val id: Int,
    val name: String,
    val avatarImage: Int // 圖片資源ID
)
data class Reviewer(
    val id: Int,
    val name: String,
    val avatarImage: Int, // 圖片資源ID
    val followers: Int = 0, // 追蹤者人數
    val following: Int = 0  // 追蹤中人數
)