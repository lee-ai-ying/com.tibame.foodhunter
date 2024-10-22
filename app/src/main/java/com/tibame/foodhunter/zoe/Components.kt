package com.tibame.foodhunter.zoe

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.tibame.foodhunter.R


@Composable
fun PostList(posts: List<Post>) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
fun FilterChips(
    filters: List<String>,
    selectedFilters: Set<String>,
    onFilterChange: (Set<String>) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilters.contains(filter)
            FilterChip(
                selected = isSelected,
                onClick = {
                    // 更新選中的篩選標籤
                    val updatedFilters = if (isSelected) {
                        selectedFilters - filter
                    } else {
                        selectedFilters + filter
                    }
                    onFilterChange(updatedFilters)
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
//                .padding(vertical = 8.dp)
            ,
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
                        .size(8.dp).zIndex(1f)
                )
            }
        }
    }
}



@Composable
fun PostItem(post: Post) {
    Log.d("PostItem", "Displaying post from: ${post.publisher}, Location: ${post.location}, Content: ${post.content}")
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

@Composable
fun ImageList(posts: List<Post>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),  // 定義每行顯示三個元素
        modifier = modifier.padding(8.dp),  // 設定網格的邊距
        verticalArrangement = Arrangement.spacedBy(20.dp),  // 設定垂直間距
        horizontalArrangement = Arrangement.spacedBy(20.dp)  // 設定水平間距
    ) {
        items(posts) { post ->
            if (post.carouselItems.isNotEmpty()) {
                ImageItem(
                    imageResId = post.carouselItems[0].imageResId,
                    contentDescription = post.carouselItems[0].contentDescription
                )
            }
        }
    }
}

@Composable
fun ImageItem(imageResId: Int, contentDescription: String) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .aspectRatio(1f)  // 確保圖片是正方形的
            .fillMaxWidth(),  // 填滿可用寬度
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}