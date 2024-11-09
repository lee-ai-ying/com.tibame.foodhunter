package com.tibame.foodhunter.a871208s

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import kotlinx.coroutines.launch


@Composable
fun FriendManagementScreen(
    navController: NavHostController = rememberNavController(),
    friendVM: FriendViewModel = viewModel()
    ,userVM: UserViewModel
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

// 使用 StateFlow 來觀察狀態變化
    val friendState by friendVM.friendState.collectAsState()
    val friendState2 by friendVM.friendState2.collectAsState()

    LaunchedEffect(Unit) {
        scope.launch {
            // 確保在畫面初次顯示時抓取好友資料
            friendVM.refreshFriends(userVM.username.value)
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            // 新增好友
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "新增好友",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "新增好友",
                        modifier = Modifier.clickable {
                            navController.navigate(context.getString(R.string.str_member) + "/7")
                        }
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.size(500.dp, 1.dp), color = Color.Blue)
        }

        item {
            Text(
                text = "已追蹤",
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
            HorizontalDivider(modifier = Modifier.size(500.dp, 1.dp), color = Color.Blue)
        }

        // 已追蹤好友列表
        items(friendState) { friend ->
            FriendListItem(friend, onItemClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(friend.nickname,withDismissAction = true)
                }
            },userVM=userVM, friendVM = friendVM) // 传递 friendVM
        }

        item {
            Text(
                text = "建議追蹤",
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
            HorizontalDivider(modifier = Modifier.size(500.dp, 1.dp), color = Color.Blue)
        }

        // 建議追蹤好友列表
        items(friendState2) { friend ->
            SFriendListItem(friend, onItemClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(friend.nickname, withDismissAction = true)
                }
            },userVM=userVM, friendVM = friendVM) // 传递 friendVM
        }
    }
}

@Composable
fun FriendListItem(friend: Friend, onItemClick: () -> Unit,userVM: UserViewModel,friendVM: FriendViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text(text = "確定要取消追蹤?") },
            confirmButton = {
                    Button(onClick = { coroutineScope.launch {
                        val friendDel = friendVM.friendDel(userVM.username.value,friend.username)
                        Log.e("Response","Response2: friendDel: $friendDel")
                        if (friendDel) {
                            // 追蹤成功後刷新好友列表
                            friendVM.refreshFriends(userVM.username.value)
                        }
                        // 隱藏對話框
                        showDialog = false
                    }
                }) { Text("確定") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("取消") }
            }
        )
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onItemClick),
        headlineContent = { Text(friend.nickname) },
        leadingContent = {
            val imageBitmap = friend.profileImageBase64?.let { base64ToBitmap(it) }
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "friend",
                    modifier = Modifier
                        .size(30.dp)
                        .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // 顯示預設圖像或占位圖像
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = "default friend image",
                    modifier = Modifier
                        .size(30.dp)
                        .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        trailingContent = {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "delete")
            }
        }
    )
}


@Composable
fun SFriendListItem(friend: Friend, onItemClick: () -> Unit,userVM: UserViewModel,friendVM: FriendViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text(text = "請確認是否要追蹤?") },
            confirmButton = {
                Button(onClick = { coroutineScope.launch {val friendAdd = friendVM.friendAdd(userVM.username.value,friend.username)
                    if (friendAdd) {
                        // 追蹤成功後刷新好友列表
                        friendVM.refreshFriends(userVM.username.value)
                    }
                    // 隱藏對話框
                    showDialog = false
                }
                }) { Text("確定") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("取消") }
            }
        )
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onItemClick),
        headlineContent = { Text(friend.nickname) },
        leadingContent = {
            val imageBitmap = friend.profileImageBase64?.let { base64ToBitmap(it) }
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "friend",
                    modifier = Modifier
                        .size(30.dp)
                        .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // 顯示預設圖像或占位圖像
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = "default friend image",
                    modifier = Modifier
                        .size(30.dp)
                        .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        trailingContent = {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "delete")
            }
        }
    )
}




fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}