package com.tibame.foodhunter.wei

import com.tibame.foodhunter.zoe.CarouselItem
import com.tibame.foodhunter.zoe.Comment
import com.tibame.foodhunter.zoe.Publisher

data class RelatedPost(
    var postId: Int,
    var publisher: Publisher,
    var content: String,
    var location: String,
    val imageRes: String,
    var postTag: String,
    )