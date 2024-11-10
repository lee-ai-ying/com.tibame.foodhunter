package com.tibame.foodhunter.wei

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.zoe.FilterChips

@Preview
@Composable
fun ReviewCompoentsPreview() {
    val navController = rememberNavController()
    val restaurantVM = SearchScreenVM() // 根據需要替換成模擬或預設的 ViewModel
    val reviewVM = ReviewVM()

    ReviewInfoDetail( reviewVM = reviewVM )
}


/**詳細評論內容*/
@Composable
fun ReviewInfoDetail(
    reviewVM: ReviewVM,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

) {
    val review by reviewVM.reviewState.collectAsState()
    val sortOrder by reviewVM.sortOrder.collectAsState()
    // 控制收藏狀態(icon圖示及snackbar文字)
    //var isBookmarked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("最新發布", "最多好評", "最高評分")

    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(120.dp)

    ) {
        Column(modifier = Modifier.weight(1f)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = { Text("在評論中搜尋") },
                onActiveChange = { isActive }
            )

            FilterChips(
                filters = listOf("服務費", "環境", "價格", "清潔"),
                selectedFilters = listOf(""),
                onFilterChange = { },
            )
        }

        // DropDownMenu開關按鈕
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = painterResource(id = R.drawable.filter_list),
                contentDescription = "評論篩選器",
                modifier = Modifier.size(30.dp)
            )
        }

        // DropDownMenu 顯示選項
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        // 根據選擇更新排序方式
                        when (option) {
                            "Newest" -> reviewVM.updateSortOrder(SortOrder.NEWEST)
                            "Most Liked" -> reviewVM.updateSortOrder(SortOrder.MOST_LIKED)
                            "Highest Rating" -> reviewVM.updateSortOrder(SortOrder.HIGHEST_RATING)
                        }
                        expanded = false // 選擇後關閉下拉選單
                    }
                )
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        //val options = listOf("複製文字", "回覆此評論", "編輯評論")

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


    }
}



/**評論顯示區*/
@Composable
fun DetailReviewZone(
    reviewVM: ReviewVM,
) {
    val review by reviewVM.reviewState.collectAsState()

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


        GetDetailReviews()
    }
}

@Composable
fun ReviewItems(review: Reviews) {
    var rememberRating by remember { mutableStateOf(review.rating) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 顯示頭像
        Image(
            painter = painterResource(id = R.drawable.image),
            contentDescription = "Reviewer Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 顯示評論者名稱
            Text(
                text = review.reviewer.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            // 顯示評分
            RatingBar(
                rating = rememberRating,
                onRatingChanged = { newRememberRating ->
                    rememberRating = newRememberRating
                }
            )
            Text(
                text = "評分: ${review.rating}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 顯示評論內容
            Text(
                text = review.content,
                fontSize = 14.sp,
                color = Color.Black
            )

            // 顯示時間戳
            Text(
                text = review.timestamp,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

/**詳細評論範例**/
@Composable
fun GetDetailReviews(
) {
    val reviews = listOf(
        Reviews(
            reviewId = 1,
            reviewer = Reviewer(
                id = 1,
                name = "使用者名稱1",
                avatarImage = null,
                followers = 10,
                following = 5
            ),
            restaurantId = 101,
            rating = 4,
            content = "這家餐廳的食物非常美味！",
            timestamp = "2024-11-01 12:00",
            isLiked = false,
            isDisliked = false,
            replies = emptyList(),
            maxPrice = 500,
            minPrice = 200,
            serviceCharge = 0
        ),
        Reviews(
            reviewId = 2,
            reviewer = Reviewer(
                id = 2,
                name = "使用者名稱2",
                avatarImage = null,
                followers = 20,
                following = 10
            ),
            restaurantId = 102,
            rating = 5,
            content = "服務態度非常好，食物也很新鮮！",
            timestamp = "2024-11-02 13:00",
            isLiked = true,
            isDisliked = false,
            replies = emptyList(),
            maxPrice = 600,
            minPrice = 300,
            serviceCharge = 2
        ),
        Reviews(
            reviewId = 3,
            reviewer = Reviewer(
                id = 3,
                name = "使用者名稱3",
                avatarImage = null,
                followers = 5,
                following = 2
            ),
            restaurantId = 103,
            rating = 3,
            content = "食物還可以，但環境有點吵。",
            timestamp = "2024-11-03 14:00",
            isLiked = false,
            isDisliked = true,
            replies = emptyList(),
            maxPrice = 400,
            minPrice = 150,
            serviceCharge = 0
        ),
        Reviews(
            reviewId = 4,
            reviewer = Reviewer(
                id = 4,
                name = "使用者名稱4",
                avatarImage = null,
                followers = 15,
                following = 7
            ),
            restaurantId = 104,
            rating = 2,
            content = "不太好，食物冷掉了。",
            timestamp = "2024-11-04 15:00",
            isLiked = false,
            isDisliked = true,
            replies = emptyList(),
            maxPrice = 300,
            minPrice = 100,
            serviceCharge = 1
        ),
        Reviews(
            reviewId = 5,
            reviewer = Reviewer(
                id = 5,
                name = "使用者名稱5",
                avatarImage = null,
                followers = 8,
                following = 3
            ),
            restaurantId = 105,
            rating = 1,
            content = "非常失望，不會再來了。",
            timestamp = "2024-11-05 16:00",
            isLiked = false,
            isDisliked = true,
            replies = emptyList(),
            maxPrice = 200,
            minPrice = 50,
            serviceCharge = 2
        )
    )

    LazyColumn {
        items(reviews) { review ->
            ReviewItems(review) // 假設有一個 ReviewItem 函數處理每個評論的顯示
            Spacer(modifier = Modifier.size(10.dp)) // 每筆評論間的間距
        }
    }
}