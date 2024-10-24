package com.tibame.foodhunter.zoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme



@Composable
fun PersonHomepage() {
    val bob = Publisher(id = "2", name = "Bob", avatarImage = R.drawable.user1, joinDate = "2023-09-01")
    val samplePosts: List<Post> = getSamplePosts()

    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        PostHeader(publisher = bob)
        ImageList(posts = samplePosts)


    }
}










@Preview(showBackground = true)
@Composable
fun PostPreview() {

    FoodHunterTheme {
        PersonHomepage()
    }
}