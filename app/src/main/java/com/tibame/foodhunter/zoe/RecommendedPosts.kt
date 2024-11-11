package com.tibame.foodhunter.zoe

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.a871208s.UserViewModel
import com.tibame.foodhunter.ui.theme.FColor
import kotlinx.coroutines.launch
@Composable
fun RecommendedPosts(
    navController: NavHostController? = null,
    postViewModel: PostViewModel = viewModel(),
    userVM: UserViewModel,
) {
    val filteredPosts by postViewModel.getFilteredPosts().collectAsState()
    val selectedFilters by postViewModel.selectedFilters.collectAsState()
    val memberId by userVM.memberId.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(memberId) {
        Log.d("RecommendedPosts", "Current memberId from userVM: $memberId")
    }

    LaunchedEffect(filteredPosts) {
        isLoading = filteredPosts.isEmpty()
    }

    Column {
        FilterChips(
            filters = listOf("早午餐", "午餐", "晚餐", "韓式", "日式", "義式", "美式", "中式", "法式", "下午茶", "甜點", "素食", "清真", "高級料理", "平價餐廳", "家庭聚餐", "快餐"),
            selectedFilters = selectedFilters,
            onFilterChange = { updatedFilters ->
                postViewModel.updateFilters(updatedFilters)
            }
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = FColor.Orange_1st
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "載入中...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FColor.Orange_1st
                    )
                }
            } else {
                PostList(
                    posts = filteredPosts,
                    viewModel = postViewModel,
                    memberId = memberId,
                    onUserClick = { publisherId ->
                        navController?.navigate("person_homepage/$publisherId") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onPostClick = { postId ->
                        postViewModel.setPostId(postId)
                        navController?.navigate("postDetail/$postId") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    navController = navController!! // 確保這裡傳遞 navController
                )
            }
        }
    }
}

@Composable
fun PostList(
    posts: List<Post>,
    viewModel: PostViewModel,
    memberId: Int,
    onUserClick: (Int) -> Unit,
    onPostClick: (Int) -> Unit,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                viewModel = viewModel,
                memberId = memberId,
                onUserClick = onUserClick,
                onPostClick = onPostClick // 將 onPostClick 傳遞給 PostItem
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
    viewModel: PostViewModel,
    memberId: Int,
    onUserClick: (Int) -> Unit,
    onPostClick: (Int) -> Unit
) {
    LaunchedEffect(memberId) {
        Log.d("PostItem", "PostItem received memberId: $memberId")
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            PostHeader(
                post = post,
                onUserClick = onUserClick,
            )

            Spacer(modifier = Modifier.height(8.dp))

            ImageDisplay(imageSource = ImageSource.CarouselSource(post.carouselItems))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FavoriteIcon()
                        IconButton(
                            onClick = {
                                Log.d("PostItem", "Comment button clicked")
                                showBottomSheet = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chat_bubble_outline_24),
                                contentDescription = "Chat Bubble",
                                modifier = Modifier.size(24.dp)
                            )

                        }

                        Text(
                            text = "${post.comments.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // 添加灰色邊框
                                .background(Color.White) // 背景顏色
                                .padding(8.dp) // 內部間距
                        ) {
                            Text(
                                text = post.postTag,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Text(
                text = post.content,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onPostClick(post.postId) // 點擊時呼叫 onPostClick
                }
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                Log.d("PostItem", "Dismissing MessageSheet")
                showBottomSheet = false
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            MessageSheet(
                post = post,
                viewModel = viewModel,
                memberId = memberId,
                onConfirm = {
                    Log.d("PostItem", "MessageSheet confirmed")
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet = false
                    }
                }
            )
        }
    }
}

@Composable
private fun PostHeader(
    post: Post,
    onUserClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            imageData = post.publisher.avatarBitmap,
            defaultImage = post.publisher.avatarImage,
            onClick = { onUserClick(post.publisher.id) },
            contentDescription = "Avatar for ${post.publisher.name}"
        )

        Column(
            modifier = Modifier.clickable { onUserClick(post.publisher.id) }
        ) {
            Text(
                text = post.publisher.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}