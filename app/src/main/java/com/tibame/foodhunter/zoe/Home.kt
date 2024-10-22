package com.tibame.foodhunter.zoe

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.Main
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val contentDescription: String
)

data class Post(
    val publisher: String,
    val content: String,
    val visibility: Int,
    val location: String,
    val publisherImage: Int,
    val postTag:String,
    val carouselItems: List<CarouselItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController = rememberNavController()) {
    val samplePosts: List<Post> = getSamplePosts()
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }

    // 根據選擇的篩選標籤過濾貼文
    val filteredPosts = if (selectedFilters.isEmpty()) {
        samplePosts
    } else {
        samplePosts.filter { post -> selectedFilters.contains(post.postTag) }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        PrimaryTabRow(selectedTabIndex = 0) {
            Tab(
                selected = true,
                onClick = { },
                text = { Text(text = stringResource(id = R.string.recommend)) }
            )
            Tab(
                selected = false,
                onClick = { },
                text = { Text(text = stringResource(id = R.string.search)) }
            )
        }

        // 使用 FilterChips 函數
        FilterChips(
            filters = listOf("早午餐", "午餐", "晚餐"),
            selectedFilters = selectedFilters,
            onFilterChange = { updatedFilters -> selectedFilters = updatedFilters }
        )

        // 顯示過濾後的貼文列表
        PostList(posts = filteredPosts)

    }
}


// 示例數據
fun getSamplePosts(): List<Post> {
    return listOf(
        Post(
            publisher = "Alice",
            content = "今天吃了美味的早餐！",
            visibility = 0,
            location = "Taipei",
            postTag = "早午餐",
            publisherImage = R.drawable.user1,
            carouselItems = listOf(
                CarouselItem(0, R.drawable.user1, "Breakfast image 1"),
                CarouselItem(1, R.drawable.breakfast_image_2, "Breakfast image 2"),
                CarouselItem(2, R.drawable.breakfast_image_3, "Breakfast image 3")
            )
        ),
        Post(
            publisher = "Bob",
            content = "午餐是超棒的壽司！",
            visibility = 1,
            location = "Kaohsiung",
            postTag = "午餐",
            publisherImage = R.drawable.user2,
            carouselItems = listOf(
                CarouselItem(0, R.drawable.sushi_image_1, "Sushi image 1"),
                CarouselItem(1, R.drawable.sushi_image_2, "Sushi image 2"),

                )
        ),
        Post(
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
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FoodHunterTheme  {
       Home()
    }
}
