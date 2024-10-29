package com.tibame.foodhunter.zoe

data class Post(
    val postId: String,
    val publisher: Publisher,
    val content: String,
    val location: String,
    val timestamp: String,
    val postTag: String,
    val carouselItems: List<CarouselItem>,
    val comments: List<Comment>,
    val isFavorited: Boolean = false

)