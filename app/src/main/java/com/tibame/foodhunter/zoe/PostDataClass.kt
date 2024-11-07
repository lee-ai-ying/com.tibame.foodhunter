package com.tibame.foodhunter.zoe

import androidx.compose.ui.graphics.ImageBitmap


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
    val publisher: Int = 0,
    var content: String = "",
    var postTag: String = "",
    var restaurantId: Int = 0,
    val photos: List<PostPhoto> = emptyList()
)

data class PostPhoto(
    val imgBase64Str: String
)