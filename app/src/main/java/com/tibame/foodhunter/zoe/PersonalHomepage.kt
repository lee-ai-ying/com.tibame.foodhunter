package com.tibame.foodhunter.zoe

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.a871208s.UserViewModel
import kotlinx.coroutines.launch
@Composable
fun PersonHomepage(
    publisherId: Int,
    postViewModel: PostViewModel,
    userVM: UserViewModel,
    navController: NavHostController
) {
    val personalPosts = postViewModel.getPersonalPosts(publisherId).collectAsState()
    val memberId by userVM.memberId.collectAsState() // 直接使用 UserViewModel 中的 memberId
    LaunchedEffect(memberId) {
        Log.d("PersonHomepage", "Current memberId from userVM: $memberId")
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        personalPosts.value.firstOrNull()?.publisher?.let { publisher ->
            PostHeader(
                publisher = publisher,
                isCurrentUser = memberId == publisherId // 使用收集到的 memberId
            )
        }

        ImageList(
            posts = personalPosts.value,
            onPostClick = { postId ->
                navController.navigate("postDetail/$postId")
            },
            navController = navController
        )
    }
}
@Composable
fun PostHeader(
    publisher: Publisher,
    isCurrentUser: Boolean
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Avatar(
                imageData = publisher.avatarBitmap,
                defaultImage = publisher.avatarImage
            )

            Text(
                text = publisher.name,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)  // 讓文字佔據剩餘空間
            )

            // 只在非本人頁面顯示私訊和追蹤按鈕
            if (!isCurrentUser) {
                IconButton(
                    onClick = { /* 點擊私訊按鈕時的處理 */ },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat_bubble_outline_24),
                        contentDescription = "Chat Bubble",
                        modifier = Modifier.size(25.dp)
                    )
                }

                Button(
                    onClick = { /* 點擊追蹤按鈕時的處理 */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.orange_1st)
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        ) {
            Text(
                text = "追蹤者: ${publisher.followers.size} | 追蹤中: ${publisher.following.size}",
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}





