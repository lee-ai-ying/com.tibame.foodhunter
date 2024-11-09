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
import androidx.compose.foundation.lazy.items
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
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.sharon.viewmodel.NoteVM
import com.tibame.foodhunter.zoe.FilterChips


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
            Text(
                text = restaurant?.address.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = restaurant?.opening_hours.toString(),
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
}


/**相關貼文*/
//@Composable
//fun RelatedPost(posts: List<Post>) {
//    LazyRow(
//        modifier = Modifier
//            .padding(4.dp)
//    ) {
//        items(posts) { post ->
//            PostItems(Post = RelatedPost)
//            Spacer(modifier = Modifier.size(8.dp)) // 每筆貼文間的間距
//        }
//    }
//}

@Composable
fun PostItems(
) {

    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                onClick = { /*導航到檢視貼文*/ },
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


