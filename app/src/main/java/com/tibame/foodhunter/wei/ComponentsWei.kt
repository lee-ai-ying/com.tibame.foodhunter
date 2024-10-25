package com.tibame.foodhunter.wei

import android.content.res.Resources.Theme
import android.view.View.OnCreateContextMenuListener
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.zoe.Post
import com.tibame.foodhunter.zoe.PostItem
import kotlinx.coroutines.launch

/**餐廳資訊*/
@Composable
fun RestaurantInfoDetail(
) {
    val snackbarHostState = remember { SnackbarHostState() }
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    // 控制收藏狀態(icon圖示及snackbar文字)
    var isBookmarked by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "餐廳名稱",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "餐廳描述1",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "餐廳描述2",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            Button(
                onClick = {
                    isBookmarked = !isBookmarked
                    val message = if (isBookmarked) {
                        "收藏成功"
                    } else {
                        "取消收藏"
                    }

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message,
                            withDismissAction = true
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isBookmarked) R.drawable.bookmark_filled else R.drawable.bookmark_border
                    ),
                    contentDescription = if (isBookmarked) "已收藏" else "未收藏",
                    modifier = Modifier.size(28.dp)
                )
            }

            Button(
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                    contentDescription = "更多功能",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }

    if (showDialog) {
        Column(
            modifier = Modifier
                .width(150.dp)
                .height(250.dp)
                .background(Color(0xFFFED2C7))
                .padding(16.dp),

            ) {

            Button(onClick = {
                // 建立揪團的功能
                showDialog = false
            }) {
                Text("建立揪團")
            }

            Button(onClick = {
                // 建立筆記的功能
                showDialog = false
            }) {
                Text("建立筆記")
            }

            Button(onClick = {
                // 建立貼文的功能
                showDialog = false
            }) {
                Text("建立貼文")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showDialog = false }) {
                Text("關閉")
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
fun Reviews() {
    //val reviewList = (0..20).toList() 建立假的評論們

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


    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {


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
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "使用者名稱",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "☆星星數☆",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            //讚、倒讚
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Button(
                    onClick = {
                        //一定是大拇指的啦，點擊+1
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_thumb_up),
                        contentDescription = "讚哦",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Button(
                    onClick = {
                        //這麼臭的評論有必要存在嗎，點擊+1
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_thumb_down),
                        contentDescription = "倒讚",
                        modifier = Modifier.size(30.dp)
                    )
                }

            }
        }
    }
}