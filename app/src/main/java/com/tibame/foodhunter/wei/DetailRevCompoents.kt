package com.tibame.foodhunter.wei

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.zoe.FilterChips
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

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
                filters = listOf("服務", "環境", "價格", "清潔"),
                selectedFilters = listOf(""),
                onFilterChange = { },
            )
        }

        Spacer(modifier = Modifier.size(10.dp))
    }
}


/**詳細評論顯示區*/
@Composable
fun DetailReviewZone(
    navController: NavHostController,
    viewModel: ReviewVM,
    restaurantId: Int,
    reviewId: Int? = null
) {
    // 當 reviewId 不為 null 時才載入評論
    LaunchedEffect(reviewId) {
        reviewId?.let { id ->
            viewModel.loadReviewById(id)
        }
    }
    val review by viewModel.reviewState.collectAsState()
    val context = LocalContext.current
    // 收集評論狀態
    val currentReview by viewModel.currentReview.collectAsState()



    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        var expanded by remember { mutableStateOf(false) }
        val options = listOf("最新發布", "最多好評", "最高評分")
        // DropDownMenu開關按鈕
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = painterResource(id = R.drawable.filter_list),
                    contentDescription = "評論篩選器",
                    modifier = Modifier.size(25.dp)
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
                                "Newest" -> viewModel.updateSortOrder(SortOrder.NEWEST)
                                "Most Liked" -> viewModel.updateSortOrder(SortOrder.MOST_LIKED)
                                "Highest Rating" -> viewModel.updateSortOrder(SortOrder.HIGHEST_RATING)
                            }
                            expanded = false // 選擇後關閉下拉選單
                        }
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier,
            thickness = 2.dp,
            color = Color(0xFFFE724C)
        )
        Spacer(modifier = Modifier.size(8.dp))
        currentReview?.let { review ->
            DetailReviewItem(review = review)
        } ?: run {
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())    // 添加垂直捲動功能
                .padding(horizontal = 16.dp)
        ) {
            // 確保從 viewModel 中載入評論，並傳遞正確的 restaurantId
            DetailReviewList(restaurantId = restaurantId, viewModel = viewModel)
        }
    }
}

/** 真正的評論 */
@Composable
fun DetailReviewList(restaurantId: Int, viewModel: ReviewVM) {
    // 當頁面載入時，載入該餐廳的評論
    LaunchedEffect(restaurantId) {
        Log.d("ReviewList", "Loading reviews for restaurantId: $restaurantId")
        viewModel.loadReviews(restaurantId)
    }

    val reviews by viewModel.reviewState.collectAsState()  // 觀察評論列表的資料變動
    Log.d("ReviewList", "Loaded reviews: ${reviews.size}")

    val reviewsWithReplies = reviews.map { review ->
        review.copy(replies = generateDummyReplies())
    }


    Column{
        reviews.forEach { review ->
            DetailReviewItem(review)
            Spacer(modifier = Modifier.size(10.dp)) // 每筆評論間的間距
        }
    }
}

@Composable
fun DetailReviewItem(review: Reviews) {
    var isLiked by remember { mutableStateOf(review?.isLiked ?: false) }
    var isDisliked by remember { mutableStateOf(review?.isDisliked ?: false) }
    var thumbsUpCount by remember { mutableStateOf(review?.thumbsup ?: 0) }
    var thumbsDownCount by remember { mutableStateOf(review?.thumbsdown ?: 0) }
    var rememberRating by remember { mutableStateOf(review.rating) }
    var showReplies by remember { mutableStateOf(false) }
    if (review == null) {
        Text(
            text = "目前尚無評論",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        return
    }

    Row {

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.size(8.dp))

            // 顯示評論者名稱
            Text(
                text = review.reviewer.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = FColor.Dark_80
            )

            Spacer(modifier = Modifier.size(5.dp))

            // 顯示評分
            RatingBar(
                rating = review.rating,
                onRatingChanged = { }  //顯示用，不需改變
            )

            // 顯示評論內容
            Text(
                text = review.content,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // 顯示評論時間
            Text(
                text = review.timestamp,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            // 按讚按鈕
            Button(
                onClick = {
                    isLiked = !isLiked
                    thumbsUpCount += if (isLiked) 1 else -1
                    if (isDisliked) {
                        isDisliked = false
                        thumbsDownCount--
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isLiked) R.drawable.baseline_thumb_up_filled
                        else R.drawable.baseline_thumb_up
                    ),
                    contentDescription = "讚",
                    modifier = Modifier.size(30.dp),
                    // 加入 tint 參數來設定顏色
                    tint = if (isLiked) FColor.Orange_1st //
                    else FColor.Dark_80 // 未選中時的顏色
                )
                Text(text = " $thumbsUpCount",
                    modifier = Modifier.padding(start = 8.dp))
            }

            // 倒讚按鈕
            Button(
                onClick = {
                    isDisliked = !isDisliked
                    thumbsDownCount += if (isDisliked) 1 else -1
                    if (isLiked) {
                        isLiked = false
                        thumbsUpCount--
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isDisliked) R.drawable.baseline_thumb_down_filled
                        else R.drawable.baseline_thumb_down
                    ),
                    contentDescription = "倒讚",
                    modifier = Modifier.size(30.dp),
                    // 加入 tint 參數來設定顏色
                    tint = if (isDisliked) FColor.Orange_1st
                    else FColor.Dark_80 // 未選中時的顏色
                )
                Text(text = " $thumbsDownCount",
                    modifier = Modifier.padding(start = 8.dp))
            }
        }
        // 展開/收起回覆的按鈕
        TextButton(
            onClick = { showReplies = !showReplies },
            colors = ButtonDefaults.textButtonColors(
                contentColor = FColor.Orange_1st
            )
        ) {
            Text(
                text = if (showReplies) "收起回覆" else "顯示回覆(${review.replies.size})",
                fontSize = 14.sp
            )
        }
    }

    // 展開時顯示回覆列表
    if (showReplies) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp)
        ) {
            review.replies.forEach { reply ->
                ReplyItem(reply)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun ReplyItem(reply: Reply) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = FColor.Orange_1st.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reply.replier.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = FColor.Dark_80
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = reply.timestamp,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = reply.content,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

// 假資料生成函數
fun generateDummyReplies(): List<Reply> {
    val replyContents = listOf(
        "餐點真的很美味，特別是他們的招牌料理！",
        "服務態度非常好，氣氛也很棒。",
        "價格稍微高了一點，但是品質絕對對得起這個價位。",
        "環境很乾淨，座位也很舒適。",
        "等待時間有點長，但食物的品質值得等待。"
    )

    val replierNames = listOf("小明", "小華", "小菁", "小芳", "小強")

    return List(5) { index ->
        Reply(
            id = index + 1,
            replier = Replier(
                id = index + 1,
                name = replierNames[index],
                avatarImage = null
            ),
            content = replyContents[index],
            timestamp = LocalDateTime.now()
                .minusDays(Random.nextLong(1, 30))
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
        )
    }
}