package com.tibame.foodhunter.a871208s

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ai_ying.GroupChat
import com.tibame.foodhunter.ai_ying.GroupSearchBar
import com.tibame.foodhunter.ai_ying.GroupViewModel

@Composable
fun PrivateChatScreen(
    navController: NavHostController,
    pChatVM: PrivateViewModel= viewModel()
) {
    var searchInput by remember { mutableStateOf("") }
    val privateChats by pChatVM.PrivateChatFlow.collectAsState()
    Column(
    ) {
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



        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {


            items(privateChats.filter { it.name.contains(searchInput) }) { privateChat ->

                Column(
                    modifier = Modifier.background(Color.White).clickable {
                        pChatVM.setDetailGroupChat(privateChat)
                        navController.navigate("GroupChatRoom/${privateChat.id}")
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
                                Image(
                                    painter = painterResource(id = privateChat.image),
                                    contentDescription = "friend",
                                    modifier = Modifier.size(30.dp)
                                        .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = privateChat.name
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
            //Spacer(modifier = Modifier.fillMaxWidth().size(1.dp).background(colorResource(R.color.orange_1st)))
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GroupChatListPreview(privateVM: PrivateViewModel = viewModel()) {
    MaterialTheme {
        val privateChats by privateVM.PrivateChatFlow.collectAsState()
        PrivateChatScreen(rememberNavController(), viewModel())
    }
}