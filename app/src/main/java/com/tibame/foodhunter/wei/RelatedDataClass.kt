package com.tibame.foodhunter.wei

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R

import com.tibame.foodhunter.zoe.PostDetail
import com.tibame.foodhunter.zoe.PostDetailScreen
import com.tibame.foodhunter.zoe.PostViewModel
import com.tibame.foodhunter.zoe.Publisher
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class RelatedPost(
    var postId: Int,
    var publisher: Publisher,
    var content: String,
    var location: String,
    val imageRes: String,
    var postTag: String,
    )

//
//@Composable
//fun RelatedPosts(
//    location: String?,
//    postViewModel: PostViewModel = viewModel()
//) {
//    val navController = rememberNavController()
    // 根據 location 從 ViewModel 中取得特定的貼文
//    val post = location?.let { postViewModel.getPostByLocation(it).collectAsState().value }
//
//    post?.let { nonNullPost ->
//        // 顯示貼文詳細內容
//        PostDetail(post = nonNullPost)
//    } ?: Column(
//        modifier = Modifier
//            .height(200.dp)
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // 當找不到貼文時顯示的訊息
//        Text(
//            text = "尚未有相關貼文",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = { navController.navigate("發文")
//             },
//            colors = ButtonDefaults.buttonColors(
//                colorResource(id = R.color.orange_2nd)
//            ),
//        ) {
//            Text("建立第一篇貼文")
//        }
//    }
//}

//
//@Preview(showBackground = true)
//@Composable
//fun RelatedPostsPreview() {
//    MaterialTheme {
//
//        RelatedPosts("台北市信義區" )
//    }
//}