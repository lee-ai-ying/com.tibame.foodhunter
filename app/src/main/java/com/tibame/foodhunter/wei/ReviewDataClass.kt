package com.tibame.foodhunter.wei



import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class ReviewCreateData(
    var reviewId: Int = 0,
    var reviewer: String="",
    var content: String = "",
    var location: String = "",
    var timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var reviewTag: String = "",
    var repliers: List<Reply>,
    var maxPrice: Int,
    var minPrice: Int,
    var serviceCharge:Boolean,
)

data class Reviews(
    var reviewId: Int,
    var reviewer: Reviewer,
    var content: String,
    var location: String,
    var timestamp: String,
    var rating: Int,
    var reviewTag: String,
    var Replies: List<Reply>,
    var maxPrice: Int,
    var minPrice: Int,
    var serviceCharge:Boolean,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,)


data class Review(val username: String, val rating: Int)

