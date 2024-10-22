package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun GroupChatList(
    groupChats: List<GroupChat>,
    navController: NavHostController
) {
    GroupSearchBar()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(groupChats) { groupChat ->
            if (groupChat.groupState == 99) {
                Column(
                    modifier = Modifier.background(Color.White)
                ) {
                    Text(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        text = groupChat.groupName
                    )
                }
            } else {
                Column(
                    modifier = Modifier.background(Color.White).clickable {
                        navController.navigate("gotoGroupChatRoom/${groupChat.groupId}")
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
                            Text(
                                text = groupChat.groupName
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = ""
                        )
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GroupChatListPreview() {
    MaterialTheme {
        GroupMain(rememberNavController())
    }
}