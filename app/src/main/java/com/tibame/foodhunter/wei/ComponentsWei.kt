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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.sharon.components.NiaTab
import com.tibame.foodhunter.sharon.components.NiaTabRow
import com.tibame.foodhunter.zoe.Avatar
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
    //snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
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
        Spacer(modifier = Modifier.weight(1f))

        /**加入收藏(擱置)*/
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
                painter = painterResource(id = R.drawable.baseline_more_vert_24),
                contentDescription = "更多功能",
                modifier = Modifier.size(30.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
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
fun RelatedPost(posts: List<Post>, navController: NavController) {
    Log.d("RelatedPost", posts.toString())
    val postListSize = posts.size
    val pagerState = rememberPagerState(pageCount = {postListSize})
    val context = LocalContext.current

    val mainDp = 160.dp
    if (posts.isEmpty()){
        Text(text = "尚無貼文", style = MaterialTheme.typography.titleLarge)
    } else {
        HorizontalPager(
            pageSize = PageSize.Fixed(144.dp),
            beyondViewportPageCount = 3,
            state = pagerState, // 控制左右間距
            modifier = Modifier
                .height(200.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White), // 背景色
            pageSpacing = 8.dp,
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            // 設定每個色塊的縮放效果
//        val width = when (page) {
//            pagerState.currentPage -> 180.dp // 大項目寬度（或用戶設定）
//            pagerState.currentPage + 1 ->  (180*0.7f).dp// 中等項目寬度
//            else -> (180*0.2f).dp // 小項目寬度
//        }

            val leftdp = when (page) {
                pagerState.currentPage -> 16.dp
                else -> 8.dp // 小項目寬度
            }
            val postId = posts[page].postId
            Log.d("postId1", postId.toString())
            Box(modifier = Modifier
                .background(Color.White)
                .width(180.dp)
                .height(184.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { navController.navigate("postDetail/${postId}") }
            ){

                Log.d("publisher1", "RelatedPost${posts[page].publisher}")
                ImageDisplay(imageSource = ImageSource.CarouselSource(posts[page].carouselItems))

                Avatar(
                    imageData = posts[page].publisher.avatarBitmap,
                    defaultImage = posts[page].publisher.avatarImage
                )

                Text(
                    text = posts[page].content,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.BottomStart).background(color = Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    reviewVM: ReviewVM
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


