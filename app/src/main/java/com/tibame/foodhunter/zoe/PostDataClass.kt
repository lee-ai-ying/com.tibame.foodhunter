package com.tibame.foodhunter.zoe

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Post(
    var postId: Int,
    var publisher: Publisher,
    var content: String,
    var location: String,
    var timestamp: String,
    var postTag: String,
    var carouselItems: List<CarouselItem>,
    var comments: List<Comment>,
    var isFavorited: Boolean = false

)

data class PostCreateData(
    var postId: Int = 0,
    var publisher: String="",
    var content: String = "",
    var location: String = "",
    var timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var postTag: String = "",
)
