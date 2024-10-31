package com.tibame.foodhunter.sharon.data

import com.tibame.foodhunter.R
import java.time.LocalDate

class Book(
    var isbn: String = "",
    var name: String = "",
    var price: Double = 0.0,
    var image: Int = R.drawable.sushi_image_1,
    var date: LocalDate = LocalDate.now() // 加入日期屬性，默認為今天日期

) {
    override fun equals(other: Any?): Boolean {
        return this.isbn == (other as Book).isbn
    }

    override fun hashCode(): Int {
        return isbn.hashCode()
    }
}