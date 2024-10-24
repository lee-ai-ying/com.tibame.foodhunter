package com.tibame.foodhunter.zoe

import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.tibame.foodhunter.R

data class Comment(
    val id: String,
    val commenter: Commenter, // 留言者
    val content: String,
    val timestamp: String
)

data class Commenter(
    val id: String,
    val name: String,
    val avatarImage: Int // 圖片資源ID
)
data class Publisher(
    val id: String,
    val name: String,
    val avatarImage: Int, // 圖片資源ID
    val joinDate: String,
    val followers: List<Publisher> = emptyList(), // 追蹤者列表
    val following: List<Publisher> = emptyList()  // 被追蹤者列表
)


data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val contentDescription: String
)

data class Post(
    val id: String,
    val publisher: Publisher,
    val content: String,
    val location: String,
    val timestamp: String,
    val postTag:String,
    val carouselItems: List<CarouselItem>
)


//貼文列表
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

fun PostHeader(publisher: Publisher) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = publisher.avatarImage),
            contentDescription = "Publisher avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = publisher.name,  // 保持使用 publisher.name
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(text = "追蹤者: ${publisher.followers.size} | 追蹤中: ${publisher.following.size}")

        }

        Button(
            onClick = { /* 點擊追蹤按鈕時的處理 */ },
            colors = ButtonDefaults.buttonColors(
                Color(0xFFF15A24)  // 修改為橘色
            ),
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text(
                text = "追蹤",
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}




//標籤篩選貼文
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



sealed class ImageSource {
    data class UriSource(val uris: List<Uri>) : ImageSource()
    data class CarouselSource(val items: List<CarouselItem>) : ImageSource()
}
//兩種格式都適用
@Composable
fun ImageDisplay(
    imageSource: ImageSource,
    modifier: Modifier = Modifier
) {
    when (imageSource) {
        is ImageSource.UriSource -> {
            ImageCarouselUri(
                uris = imageSource.uris,

            )
        }
        is ImageSource.CarouselSource -> {
            ImageCarouselResource(
                items = imageSource.items,
                modifier = modifier
                    .width(394.dp)
                    .height(319.dp)
            )
        }
    }
}
//從Uri拿照片
@Composable
private fun ImageCarouselUri(uris: List<Uri>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { uris.size })

    ImageCarouselLayout(
        pageCount = uris.size,
        pagerState = pagerState,
        modifier = modifier
    ) { page ->
        Image(
            painter = rememberAsyncImagePainter(uris[page]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
//拿貼文照片
@Composable
private fun ImageCarouselResource(items: List<CarouselItem>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { items.size })

    ImageCarouselLayout(
        pageCount = items.size,
        pagerState = pagerState,
        modifier = modifier
    ) { page ->
        Image(
            painter = painterResource(id = items[page].imageResId),
            contentDescription = items[page].contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
//左右滑動的圖片顯示
@Composable
private fun ImageCarouselLayout(
    pageCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            content(page)
        }

        // 指示器
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
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
                        .zIndex(1f)
                )
            }
        }
    }
}
//貼文部分
@Composable
fun PostItem(post: Post) {
    Log.d("PostItem", "Displaying post from: ${post.publisher.name}, Location: ${post.location}, Content: ${post.content}")

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
                    painter = painterResource(id = post.publisher.avatarImage),
                    contentDescription = "Publisher avatar",
                    contentScale = ContentScale.Crop, // 確保圖片被裁剪成圓形
                    modifier = Modifier
                        .size(30.dp) // 設置圖片的大小
                        .clip(CircleShape) // 裁剪成圓形
                )
            }

            Column {
                Text(text = post.publisher.name)  // 使用發文者名稱
                Text(text = post.location)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 顯示圖片輪播
        ImageDisplay(imageSource = ImageSource.CarouselSource(post.carouselItems))

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
                FavoriteIcon()

                IconButton(onClick = {
                    // 處理點擊 chat bubble 的事件
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat_bubble_outline_24),
                        contentDescription = "chat_bubble",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),  // 使用你的 bookmark 資源
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

@Composable
fun FavoriteIcon() {
    // 使用 remember 保存按下的狀態
    var isFavorite by remember { mutableStateOf(false) }

    // IconButton 包裹 Icon
    IconButton(onClick = {
        isFavorite = !isFavorite  // 切換狀態
    }) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            ),
            contentDescription = if (isFavorite) "favorite" else "not_favorite",
            modifier = Modifier.size(22.dp)
        )
    }
}




