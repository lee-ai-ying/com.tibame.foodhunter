package com.tibame.foodhunter.zoe

data class Post(
    val publisher: String,
    val content: String,
    val visibility: Int,
    val location: String,
    val publisherImage: Int,
    val postTag:String,
    val carouselItems: List<CarouselItem>
)