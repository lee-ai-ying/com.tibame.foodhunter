package com.tibame.foodhunter.wei

import android.util.Log
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.zIndex
import com.tibame.foodhunter.R
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.zoe.Post
import com.tibame.foodhunter.zoe.PostItem
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewInfoDetail() {
    ReviewZone()

}

/**餐廳資訊*/
@Composable
fun RestaurantInfoDetail(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    restaurantVM: SearchScreenVM
) {
    var isBookmarked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("建立揪團", "建立筆記", "建立貼文")
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    // 控制收藏狀態(icon圖示及snackbar文字)

    // 選擇到的餐廳詳細
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
                text = restaurant?.home_phone.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.weight(0.5f)
        ) {

            //加入收藏
            IconButton(
                onClick = {
                    isBookmarked = !isBookmarked
                    val message = if (isBookmarked) {
                        "收藏成功"
                    } else {
                        "取消收藏"
                    }

                    scope.launch {
                        Log.e("TAG", "showSnackBar")
                        snackbarHostState.showSnackbar(
                            message,
                            withDismissAction = true
                        )
                    }
                }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(if (isBookmarked) R.drawable.bookmark_filled else R.drawable.bookmark_border),
                    contentDescription = if (isBookmarked) "已收藏" else "未收藏",
                )
            }
        }


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
    LazyColumn(
        modifier = Modifier
            .size(150.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

/**評論顯示區*/
@Composable
fun ReviewZone() {
    //val reviewList = (0..20).toList() 建立假的評論們
    var rememberRating by remember { mutableStateOf(0) }
    //記錄讚數&倒讚數狀態
    var likeCount by remember { mutableStateOf(0) }
    var dislikeCount by remember { mutableStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }

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

        Button(
            onClick = {
                // 還沒寫 切到評論頁面
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

        //評論內容
        Row {
            Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "評論者",
                modifier = Modifier
                    .size(70.dp)
                    .border(BorderStroke(2.dp, Color(0xFFB43310)), CircleShape)
                    .clip(RoundedCornerShape(12)),
                contentScale = ContentScale.Crop
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "使用者名稱",
                    fontSize = 20.sp,
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

            //讚、倒讚
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f),
            ) {
                Button(
                    onClick = {
                        isLiked = !isLiked
                        likeCount += if (isLiked) 1 else -1
                        // 如果按下讚的時候已經有倒讚，則取消倒讚
                        if (isDisliked) {
                            isDisliked = false
                            dislikeCount-- // 取消倒讚
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) { //一定是大拇指的啦，點擊+1
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
                        // 如果按下倒讚的時候已經有讚，則取消讚
                        if (isLiked) {
                            isLiked = false
                            likeCount-- // 取消讚
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Icon( //這麼臭的評論有必要存在嗎，點擊+1
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

}


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
                    .size(40.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}




