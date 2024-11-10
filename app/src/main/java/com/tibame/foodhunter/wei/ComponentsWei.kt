package com.tibame.foodhunter.wei

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.andysearch.SearchScreenVM
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.andysearch.IsOpenNow
import com.tibame.foodhunter.sharon.components.NiaTab
import com.tibame.foodhunter.sharon.components.NiaTabRow
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.sharon.viewmodel.NoteVM
import com.tibame.foodhunter.zoe.FilterChips
import com.tibame.foodhunter.zoe.ImageDisplay
import com.tibame.foodhunter.zoe.ImageSource
import com.tibame.foodhunter.zoe.Post


@Preview
@Composable
fun PreviewInfoDetail() {
    //RelatedPost()

}

/**餐廳資訊*/
@Composable
fun RestaurantInfoDetail(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    restaurantVM : SearchScreenVM
) {
    var isBookmarked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("建立揪團", "建立筆記", "建立貼文")
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    // 控制收藏狀態(icon圖示及snackbar文字)
    val navController = rememberNavController()

    val restaurant by restaurantVM.choiceOneRest.collectAsState()

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
                text = restaurant?.name.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
//            Text(
//                text = restaurant?.address.toString(),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
//            Text(
//                text = restaurant?.opening_hours.toString(),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
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
                        when (option) {
                            "建立揪團" -> navController.navigate("建立揪團")
                            "建立筆記" -> navController.navigate("手札")
                            "建立貼文" -> navController.navigate("發文")
                        }
                    }
                )
            }
        }
    }

    var selectedTab by remember { mutableIntStateOf(-1) }
    val titles = listOf("營業時間", "電話", "地址")
    NiaTabRow(selectedTabIndex = if (selectedTab == -1) 0 else selectedTab,
        modifier = Modifier.fillMaxWidth()) {
        titles.forEachIndexed { index, title ->
            NiaTab(
                selected = selectedTab == index,
                onClick = { selectedTab = if (selectedTab == index) -1 else index },
                text = { Text(text = title) },
            )
        }
    }
    if (selectedTab != -1){
        when (selectedTab) {
            0 -> {
                // 營業時間
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    restaurant?.opening_hours?.let { openingHours ->
                        openingHours.split(";").forEach { dayHours ->
                            Text(
                                text = dayHours,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
            1 -> {
                // 電話
                Text(
                    text = restaurant?.home_phone ?: "無電話資訊",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
            2 -> {
                // 地址
                Text(
                    text = restaurant?.address ?: "無地址資訊",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

/**相關貼文*/
@Composable
fun RelatedPost(posts: List<Post>) {
    Log.d("RelatedPost", posts.toString())
    val postListSize = posts.size
    val pagerState = rememberPagerState(pageCount = {postListSize})

    val mainDp = 160.dp
    HorizontalPager(
        pageSize = PageSize.Fixed(200.dp),
        beyondViewportPageCount = 3,
        state = pagerState, // 控制左右間距
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp) // 上下間距
            .background(FColor.Orange_6th), // 背景色
        pageSpacing = 4.dp,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        // 設定每個色塊的縮放效果
        val width = when (page) {
            pagerState.currentPage -> 180.dp // 大項目寬度（或用戶設定）
            pagerState.currentPage + 1 ->  (180*0.7f).dp// 中等項目寬度
            else -> (180*0.2f).dp // 小項目寬度
        }
        val leftdp = when (page) {
            pagerState.currentPage -> 16.dp
            else -> 8.dp // 小項目寬度
        }
        Column(modifier = Modifier
            .background(Color.White)
            .width(width)
            .height(184.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ImageDisplay(imageSource = ImageSource.CarouselSource(posts[page].carouselItems),
                modifier = Modifier.padding(4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    noteViewModel: NoteVM
) {
//    BaseTopBar(
//        navController = navController,
//        scrollBehavior = scrollBehavior,
//        onSearchQueryChange = {
//            noteViewModel.selectedFilters
//        },
//        onFilter = {
//            noteViewModel.handleFilter(listOf())
//        },
//
}

@Composable
fun ReviewInfoDetail(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var isBookmarked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("複製文字", "回覆此評論", "編輯評論")
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    // 控制收藏狀態(icon圖示及snackbar文字)
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

        //篩選器
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = painterResource(id = R.drawable.filter_list),
                contentDescription = "篩選器排序",
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


    }
}



/**評論顯示區*/
@Composable
fun DetailReviewZone() {

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
            painter = painterResource(id = review.reviewer.avatarImage),
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
fun GetDetailReviews() {
    val reviews = listOf(
        Reviews(
            reviewId = 1,
            reviewer = Reviewer(
                id = 1,
                name = "使用者名稱1",
                avatarImage = R.drawable.account_circle,
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
            serviceCharge = false
        ),
        Reviews(
            reviewId = 2,
            reviewer = Reviewer(
                id = 2,
                name = "使用者名稱2",
                avatarImage = R.drawable.account_circle,
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
            serviceCharge = true
        ),
        Reviews(
            reviewId = 3,
            reviewer = Reviewer(
                id = 3,
                name = "使用者名稱3",
                avatarImage = R.drawable.account_circle,
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
            serviceCharge = false
        ),
        Reviews(
            reviewId = 4,
            reviewer = Reviewer(
                id = 4,
                name = "使用者名稱4",
                avatarImage = R.drawable.account_circle,
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
            serviceCharge = false
        ),
        Reviews(
            reviewId = 5,
            reviewer = Reviewer(
                id = 5,
                name = "使用者名稱5",
                avatarImage = R.drawable.account_circle,
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
            serviceCharge = false
        )
    )

    LazyColumn {
        items(reviews) { review ->
            ReviewItems(review) // 假設有一個 ReviewItem 函數處理每個評論的顯示
            Spacer(modifier = Modifier.size(10.dp)) // 每筆評論間的間距
        }
    }
}
