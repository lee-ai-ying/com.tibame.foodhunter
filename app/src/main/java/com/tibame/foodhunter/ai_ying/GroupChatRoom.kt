package com.tibame.foodhunter.ai_ying

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R


@Composable
fun GroupChatRoom(
    groupRoomId: Int,
    gChatVM: GroupViewModel
) {
    gChatVM.gotoChatRoom(groupRoomId)
    val item = listOf(
        "111",
        "222\n222",
        "333",
        "4444",
        "55555",
        "666666",
        "7777777",
        "0000000000\n88888888",
        "999999999",
        "0000000000",
        "111",
        "222",
        "333",
        "444",
        "555",
        "666",
        "777",
        "888",
        "999",
        "000"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        reverseLayout = true
    ) {
        items(item) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)//colorResource(R.color.orange_6th))
                    .padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (it) {
                    "111" -> {}
                    else ->
                        Box(
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.user1),
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, bottom = 8.dp),
                    horizontalAlignment = when (it) {
                        "111" -> Alignment.End
                        else -> Alignment.Start
                    }
                ) {
                    when (it) {
                        "111" -> {}
                        else -> Text("name")
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom
                    )
                    {
                        if (it == "111") {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                                text = "AM11:11",
                                fontSize = 12.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .background(
                                    when (it) {
                                        "111" -> MaterialTheme.colorScheme.primaryContainer//colorResource(R.color.orange_4th)
                                        else -> Color.White
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = it
                            )
                        }
                        if (it != "111") {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                                text = "AM11:11",
                                fontSize = 12.sp
                            )
                        }
                    }

                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatRoomTopBar(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    gChatVM: GroupViewModel
) {
    val chatRoom = gChatVM.chatRoom.collectAsState()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = chatRoom.value.groupName,
                color = Color.White//colorResource(R.color.orange_1st)
            )
        },
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.str_back),
                modifier = Modifier.clickable {
                    gChatVM.chatRoomInput("")
                    navController.popBackStack()
                },
                tint = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary//colorResource(R.color.orange_2nd)
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GroupChatRoomBottomBar(
    gChatVM: GroupViewModel
) {
    val chatInput = gChatVM.chatInput.collectAsState()
    var isShow by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    isShow=!isShow
                    Log.d("ai",isShow.toString())
                }
            )
            TextField(
                value = chatInput.value,
                onValueChange = {
                    gChatVM.chatRoomInput(it)
                },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,//colorResource(R.color.orange_4th),
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer//colorResource(R.color.orange_4th)
                ),
                placeholder = {
                    Text(text = "請輸入訊息")
                }
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        if (isShow){
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                FlowRow (
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    listOf("aaa","bbb","ccc","ddd","eee","fff").forEach {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.22f)
                                .padding(8.dp),//.background(Color.Cyan),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupChatRoomPreview() {
    MaterialTheme {
//        GroupChatRoom(0, viewModel())
        GroupChatRoomBottomBar(viewModel())
    }
}