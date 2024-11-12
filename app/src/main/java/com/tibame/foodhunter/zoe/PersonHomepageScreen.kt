package com.tibame.foodhunter.zoe

import android.annotation.SuppressLint
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
import androidx.compose.material3.AlertDialog
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
import com.tibame.foodhunter.a871208s.FriendViewModel
import com.tibame.foodhunter.a871208s.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun PersonHomepageScreen(
    publisherId: Int,
    postViewModel: PostViewModel,
    userVM: UserViewModel,
    navController: NavHostController,
    friendVM: FriendViewModel = viewModel()
) {
    val personalPosts = postViewModel.getPersonalPosts(publisherId).collectAsState()
    val memberId by userVM.memberId.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var publisherUsername by remember { mutableStateOf<String?>(null) }
    val friendList by friendVM.friendState.collectAsState()
    val isFollowing = remember(friendList, publisherUsername) {
        publisherUsername?.let { username ->
            friendList.any { it.username == username }
        } ?: false
    }
    // 取得發布者的 username
    LaunchedEffect(publisherId, memberId) {
        Log.d("PersonHomepage", "=== 用戶資訊檢查 ===")
        Log.d("PersonHomepage", "publisherId: $publisherId")
        Log.d("PersonHomepage", "memberId: $memberId")

        if (publisherId > 0) {
            try {
                Log.d("PersonHomepage", "開始獲取 username")
                publisherUsername = userVM.getMemberUsername(publisherId)
                Log.d("PersonHomepage", "取得發布者 username: $publisherUsername")

                // 刷新好友列表
                friendVM.refreshFriends(userVM.username.value)
            } catch (e: Exception) {
                Log.e("PersonHomepage", "取得資料時發生錯誤", e)
            }
        } else {
            Log.e("PersonHomepage", "publisherId 無效")
        }
    }
    // 檢查當前登入用戶
    LaunchedEffect(Unit) {
        Log.d("PersonHomepage", "=== 當前登入用戶檢查 ===")
        Log.d("PersonHomepage", "username: ${userVM.username.value}")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text(text = "無此帳號或已追蹤該好友") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("確定")
                }
            }
        )
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
                isCurrentUser = memberId == publisherId,
                isFollowing = isFollowing,  // 傳入追蹤狀態
                onFollowClick = {
                    coroutineScope.launch {
                        val currentUsername = userVM.username.value
                        publisherUsername?.let { targetUsername ->
                            Log.d("PersonHomepage", "嘗試追蹤好友 - 當前用戶: $currentUsername, 目標用戶: $targetUsername")
                            val success = friendVM.friendAdd(currentUsername, targetUsername)
                            if (success) {
                                // 重新刷新好友列表
                                friendVM.refreshFriends(currentUsername)
                            } else {
                                showDialog = true
                            }
                        }
                    }
                }
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
    isCurrentUser: Boolean,
    onFollowClick: () -> Unit,
    isFollowing: Boolean,
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
                modifier = Modifier.weight(1f)
            )

            if (!isCurrentUser) {
                if (!isCurrentUser) {
//                    IconButton(
//                        onClick = { /* 點擊私訊按鈕時的處理 */ },
//                        modifier = Modifier.padding(end = 8.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.chat_bubble_outline_24),
//                            contentDescription = "Chat Bubble",
//                            modifier = Modifier.size(25.dp)
//                        )
//                    }

                    Button(
                        onClick = { if (!isFollowing) onFollowClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowing)
                                Color.Gray
                            else
                                colorResource(id = R.color.orange_1st)
                        ),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = if (isFollowing) "已追蹤" else "追蹤",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}