package com.tibame.foodhunter.zoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme



@Composable
fun PersonHomepage() {
    // 構建一個 Post 對象
    val samplePost = Post(
        publisher = "Cathy",
        content = "晚餐牛排超好吃！",
        visibility = 0,
        location = "Taichung",
        postTag = "晚餐",
        publisherImage = R.drawable.user3,
        carouselItems = listOf(
            CarouselItem(0, R.drawable.steak_image, "Steak image 1"),

            )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // 傳入 samplePost 作為參數
        PostHeader(post = samplePost)
    }
}










@Preview(showBackground = true)
@Composable
fun PostPreview() {

    FoodHunterTheme {
        PersonHomepage()
    }
}