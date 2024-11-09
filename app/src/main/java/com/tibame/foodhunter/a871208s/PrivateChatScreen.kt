package com.tibame.foodhunter.a871208s

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ai_ying.GroupSearchBar
import kotlinx.coroutines.launch

@Composable
fun PrivateChatScreen(
    navController: NavHostController,
    pChatVM: PrivateViewModel,
    userVM: UserViewModel
) {
    var searchInput by remember { mutableStateOf("") }
    val privateChats by pChatVM.PrivateChatFlow.collectAsState()
    val privateChats2 by pChatVM.PrivateChatFlow2.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            // 確保在畫面初次顯示時抓取好友資料
            pChatVM.refreshchatroom(userVM.username.value)
        }
    }
    Column {
        Column(
            modifier = Modifier.background(colorResource(R.color.orange_3rd))
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                text = "聊天"
            )
        }
        GroupSearchBar(
            onValueChange = {
                searchInput = it
                searchInput
            },
            onClearClick = {
                searchInput = ""
            }
        )


        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {


            items(privateChats.filter { it.nickname.contains(searchInput) }) { privateChat ->

                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .clickable {
                            pChatVM.setDetailPrivateChat(privateChat)
                            navController.navigate("PrivateChatRoom/" + privateChat.roomid)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val imageBitmap =
                                    privateChat.profileImageBase64?.let { base64ToBitmap(it) }
                                imageBitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "friend",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .border(
                                                BorderStroke(1.dp, Color(0xFFFF9800)),
                                                CircleShape
                                            )
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
                                            .border(
                                                BorderStroke(1.dp, Color(0xFFFF9800)),
                                                CircleShape
                                            )
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Text(
                                    text = privateChat.nickname
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }

                }
            }
            if (pChatVM.wall.value == false) {
                items(privateChats2.filter { it.nickname.contains(searchInput) }) { privateChat ->

                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .clickable {
                                pChatVM.setDetailPrivateChat(privateChat)
                                navController.navigate("PrivateChatRoom/" + privateChat.roomid)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .height(56.dp)
                                .padding(start = 10.dp, end = 10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val imageBitmap =
                                        privateChat.profileImageBase64?.let { base64ToBitmap(it) }
                                    imageBitmap?.let {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = "friend",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .border(
                                                    BorderStroke(1.dp, Color(0xFFFF9800)),
                                                    CircleShape
                                                )
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
                                                .border(
                                                    BorderStroke(1.dp, Color(0xFFFF9800)),
                                                    CircleShape
                                                )
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Text(
                                        text = privateChat.nickname
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = ""
                            )
                        }

                    }
                }
            }


        }

    }

}



