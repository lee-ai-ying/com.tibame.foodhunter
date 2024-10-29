//package com.tibame.foodhunter.zoe//package com.tibame.foodhunter.zoe
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.tibame.foodhunter.R
//import com.tibame.foodhunter.ai_ying.GroupViewModel
//import com.tibame.foodhunter.ui.theme.FoodHunterTheme
//
//@Composable
//fun PersonHomepage(   postId: Int,
//                      postViewModel: PostViewModel = viewModel()
//) {
//    // 從 samplePublishers 中找到 id 為 "user1" 的 Publisher
//    postViewModel.gotoPost(postId)
//
//    val publisher by postViewModel.publisher.collectAsState()
//    Column(
//        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.Top),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Spacer(modifier = Modifier.height(20.dp))
//        PostHeader(publisher =)
//        ImageList(posts = samplePosts)
//    }
//}
//
//
//
//@Composable
//
//fun PostHeader(publisher: Publisher) {
//    Column() {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Image(
//                painter = painterResource(id = publisher.avatarImage),
//                contentDescription = "Publisher avatar",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(70.dp)
//                    .clip(CircleShape)
//
//            )
//
//
//                Text(
//                    text = publisher.name,
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//
//            IconButton(onClick = {
//
//                /* 點擊私訊按鈕時的處理 */
//
//            }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.chat_bubble_outline_24),
//                    contentDescription = "Chat Bubble",
//                    modifier = Modifier.size(25.dp)
//                )
//            }
//
//            Button(
//                onClick = { /* 點擊追蹤按鈕時的處理 */ },
//                colors = ButtonDefaults.buttonColors(
//                    colorResource(id = R.color.orange_1st)
//                ),
//                modifier = Modifier.padding(end = 16.dp)
//            ) {
//                Text(
//                    text = "追蹤",
//                    color = Color.White,
//                    style = TextStyle(
//                        fontSize = 14.sp
//                    )
//                )
//            }
//        }
//    }
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(5.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(text = "追蹤者: ${publisher.followers.size} | 追蹤中: ${publisher.following.size}")
//    }
//}
//
//
//
//
//
//
//
//
//@Preview(showBackground = true)
//@Composable
//fun PostPreview() {
//
//    FoodHunterTheme {
//        PersonHomepage()
//    }
//}