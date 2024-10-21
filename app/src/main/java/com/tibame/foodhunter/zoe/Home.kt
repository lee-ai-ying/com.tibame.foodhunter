package com.tibame.foodhunter.zoe

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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController = rememberNavController(), callback: @Composable () -> Unit) {
    val samplePosts: List<Post> = getSamplePosts()

    // 狀態用於儲存選中的篩選標籤
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }

    // 過濾貼文，根據 postTag 來篩選符合條件的貼文
    val filteredPosts = if (selectedFilters.isEmpty()) {
        samplePosts // 沒有選擇篩選時，顯示所有貼文
    } else {
        samplePosts.filter { post -> selectedFilters.contains(post.postTag) }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()

    ) {
        // TabRow 區塊
        PrimaryTabRow(selectedTabIndex = 0) {
            Tab(
                selected = true,
                onClick = { },
                text = {
                    Text(
                        text = stringResource(id= R.string.recommend),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
            Tab(
                selected = false,
                onClick = { },
                text = { Text(text = stringResource(id= R.string.search),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis) }
            )
        }

        // 篩選區塊
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val filters = listOf("早午餐", "午餐", "晚餐")
            filters.forEach { filter ->
                val isSelected = selectedFilters.contains(filter)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        // 更新選中的篩選標籤
                        selectedFilters = if (isSelected) {
                            selectedFilters - filter
                        } else {
                            selectedFilters + filter
                        }
                    },
                    label = { Text(filter) },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }

        // 顯示過濾後的貼文列表
        PostList(posts = filteredPosts)
    }
}

@Composable
fun PostList(posts: List<Post>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
fun ImageCarousel(images: List<CarouselItem>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        modifier = modifier.fillMaxWidth(), // 保持外部容器填充寬度
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()  // 填滿寬度
                .fillMaxHeight()  // 指定具體的高度
        ) { page ->
            Image(
                painter = painterResource(id = images[page].imageResId),
                contentDescription = images[page].contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 指示器
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}



@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // User info section
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = post.publisherImage),
                    contentDescription = "Publisher avatar",
                    contentScale = ContentScale.Crop, // 確保圖片被裁剪成圓形
                    modifier = Modifier
                        .size(30.dp) // 設置圖片的大小
                        .clip(CircleShape) // 裁剪成圓形
                )
            }


            Column {
                Text(text = post.publisher)
                Text(text = post.location)
            }
        }



        Spacer(modifier = Modifier.height(8.dp))

        // 使用輪播顯示圖片
        ImageCarousel(images = post.carouselItems)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .width(350.dp)
                .height(52.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 用於顯示 Favorite 和 Check 圖標的 Row
            Row(
                modifier = Modifier.weight(1f), // 使這個 Row 占據剩餘空間
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = "Favorite" // 添加有效的內容描述
                )
                Icon(
                    painter = painterResource(id = R.drawable.chat_bubble),  // 使用你的 bookmark 資源
                    contentDescription = "chat_bubble",
                    modifier = Modifier.size(22.dp)  // 可以調整大小
                )
            }

            // 靠右的 Edit 圖標

                Icon(
                    painter = painterResource(id = R.drawable.bookmark),  // 使用你的 bookmark 資源
                    contentDescription = "Bookmark",
                    modifier = Modifier.size(22.dp)  // 可以調整大小
                )

        }

        Text(
            text = post.content,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

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
            postTag = "早餐",
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
        Main()
    }
}
