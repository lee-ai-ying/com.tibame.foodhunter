package com.tibame.foodhunter.wei

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.zoe.Post
import androidx.compose.ui.text.style.TextOverflow

@Preview
@Composable
fun PreviewInfoDetail() {
    //RelatedPost()

}

/**餐廳資訊*/
@Composable
fun RestaurantInfoDetail(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var isBookmarked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("建立揪團", "建立筆記", "建立貼文")
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    // 控制收藏狀態(icon圖示及snackbar文字)


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.mapimage),
            contentDescription = "餐廳照片",
            modifier = Modifier
                .size(100.dp)
                .border(BorderStroke(3.dp, Color(0xFF000000)), RoundedCornerShape(12))
                .clip(RoundedCornerShape(12)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            //星星

            Text(
                text = "餐廳名稱",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "描述1(地址)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "描述2(電話、營業狀態)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.size(10.dp))

        /**加入收藏(暫時擱置)*/
//        Column(
//            horizontalAlignment = Alignment.End,
//            verticalArrangement = Arrangement.Top,
//            modifier = Modifier.weight(0.5f)
//        ) {
//
//            //加入收藏
//            IconButton(
//                onClick = {
//                    isBookmarked = !isBookmarked
//                    val message = if (isBookmarked) {
//                        "收藏成功"
//                    } else {
//                        "取消收藏"
//                    }
//
//                    scope.launch {
//                        Log.e("TAG", "showSnackBar")
//                        snackbarHostState.showSnackbar(
//                            message,
//                            withDismissAction = true
//                        )
//                    }
//                }) {
//                Icon(
//                    modifier = Modifier.size(30.dp),
//                    painter = painterResource(if (isBookmarked) R.drawable.bookmark_filled else R.drawable.bookmark_border),
//                    contentDescription = if (isBookmarked) "已收藏" else "未收藏",
//                )
//            }
//        }

        //更多功能
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                contentDescription = "更多功能",
                modifier = Modifier.size(30.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // 下拉選單內容由DropdownMenuItem選項元件組成
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    // 點選項目後呼叫
                    onClick = {
                        // 跳到各功能
                        expanded = false
                    }
                )
            }
        }
    }
}


/**相關貼文*/
@Composable
fun RelatedPost(posts: List<Post>) {
    LazyRow(
        modifier = Modifier
            .padding(4.dp)
    ) {
        items(posts) { post ->
            com.tibame.foodhunter.wei.PostItems(Post = post)
            Spacer(modifier = Modifier.size(8.dp)) // 每筆貼文間的間距
        }
    }
}

@Composable
fun PostItems(Post: Post) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
        //elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "發文者",
                modifier = Modifier
                    .size(30.dp)
                    .border(BorderStroke(2.dp, FColor.Orange_d1), CircleShape)
                    .clip(RoundedCornerShape(12)),
                contentScale = ContentScale.Crop
            )
                Text(
                    text = "發文者",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(id = R.drawable.steak_image),
                contentDescription = "貼文圖片",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "發文內容", maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* TODO: 跳轉至貼文頁面 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "前往貼文頁面",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**評論顯示區*/
@Composable
fun ReviewZone() {

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {

        HorizontalDivider(
            modifier = Modifier,
            thickness = 2.5.dp,
            color = Color(0xFFFE724C)
        )
        Spacer(modifier = Modifier.size(8.dp))

        Button( // 點擊後導航到 ReviewDetail頁面
            onClick = {
                // navController.navigate("ReviewDetail")
            },
            modifier = Modifier
                .width(140.dp)
                .height(50.dp),

            shape = ButtonDefaults.outlinedShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFED2C7),
                contentColor = Color(0xFFB43310)
            )
        ) {
            Text(
                text = "顯示所有評論",
                modifier = Modifier,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,

                )
        }
        GetReviews()
    }
}


@Composable
fun GetReviews() {
    val reviews = listOf(
        Review("使用者名稱1", 4),
        Review("使用者名稱2", 5),
        Review("使用者名稱3", 3),
        Review("使用者名稱4", 2),
        Review("使用者名稱5", 1)
    )

    LazyColumn {
        items(reviews) { review ->
            ReviewItem(review)
            Spacer(modifier = Modifier.size(10.dp)) // 每筆評論間的間距
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    var rememberRating by remember { mutableStateOf(review.rating) }
    var likeCount by remember { mutableStateOf(0) }
    var dislikeCount by remember { mutableStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }

    Row {
        Image(
            painter = painterResource(id = R.drawable.account_circle),
            contentDescription = "評論者",
            modifier = Modifier
                .size(70.dp)
                .border(BorderStroke(2.dp, FColor.Orange_d1), CircleShape)
                .clip(RoundedCornerShape(12)),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = review.username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.size(5.dp))

            RatingBar(
                rating = rememberRating,
                onRatingChanged = { newRememberRating ->
                    rememberRating = newRememberRating
                }
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier,
        ) {
            Button(
                onClick = {
                    isLiked = !isLiked
                    likeCount += if (isLiked) 1 else -1
                    if (isDisliked) {
                        isDisliked = false
                        dislikeCount--
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isLiked) R.drawable.baseline_thumb_up_filled else R.drawable.baseline_thumb_up
                    ),
                    contentDescription = "讚哦",
                    modifier = Modifier.size(30.dp)
                )
                Text(text = " $likeCount", modifier = Modifier.padding(start = 8.dp))
            }

            Button(
                onClick = {
                    isDisliked = !isDisliked
                    dislikeCount += if (isDisliked) 1 else -1
                    if (isLiked) {
                        isLiked = false
                        likeCount--
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isDisliked) R.drawable.baseline_thumb_down_filled else R.drawable.baseline_thumb_down
                    ),
                    contentDescription = "倒讚",
                    modifier = Modifier.size(30.dp)
                )
                Text(text = " $dislikeCount", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

data class Review(val username: String, val rating: Int)


/**星星*/
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {


    Row(modifier = modifier) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(
                    id = if (i <= rating) R.drawable.baseline_star else R.drawable.baseline_star_outline
                ),
                contentDescription = "$i 顆星",
                modifier = Modifier
                    .size(35.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}

@Composable
fun ReviewFilterChips(
    filters: List<String>,                 // 可用的標籤列表
    selectedFilters: List<String>,         // 當前選中的標籤
    onFilterChange: (List<String>) -> Unit // 選中狀態變更時的回調
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(filters) { filter ->
            val isSelected = selectedFilters.contains(filter)

            // 使用 FilterChip 或 Chip 來表示篩選標籤
            FilterChip(
                selected = isSelected,
                onClick = {
                    val updatedFilters = if (isSelected) {
                        selectedFilters - filter
                    } else {
                        selectedFilters + filter
                    }
                    onFilterChange(updatedFilters)
                },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    selectedContainerColor = (colorResource(R.color.orange_5th)),
                )
            )
        }
    }
}


