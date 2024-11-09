package com.tibame.foodhunter.zoe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.a871208s.UserViewModel
import kotlinx.coroutines.launch

enum class SheetContent {
    NONE,
    MESSAGE,
    EDIT
}
@Composable
fun PostDetailScreen(
    postId: Int?,
    navController: NavHostController,
    postViewModel: PostViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
) {
    // 獲取當前用戶 ID，這裡應該從你的用戶管理系統獲取
    val currentUserId = 7
    // 根據 postId 從 ViewModel 中取得特定的貼文
    val post = postId?.let { postViewModel.getPostById(it).collectAsState().value }

    post?.let { nonNullPost ->
        // 顯示貼文詳細內容，傳入必要的參數
        PostDetail(
            post = nonNullPost,
            viewModel = postViewModel,
            currentUserId = currentUserId ?: -1,
            navController = navController
        )
    } ?: Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 當找不到貼文時顯示的訊息
        Text(
            text = "找不到相關貼文",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.orange_2nd)
            )
        ) {
            Text("返回")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetail(
    post: Post,
    viewModel: PostViewModel,
    currentUserId: Int,
    navController: NavHostController
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentSheet by remember { mutableStateOf(SheetContent.NONE) }

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PostDetailItem(
            post = post,
            currentUserId = currentUserId,  // 傳入當前用戶ID
            onEditClick = {
                currentSheet = SheetContent.EDIT
                showBottomSheet = true
            },
            onMessageClick = {
                currentSheet = SheetContent.MESSAGE
                showBottomSheet = true
            },
            onUserClick = { publisherId ->
                navController?.navigate("person_homepage/$publisherId") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                currentSheet = SheetContent.NONE
            },
            containerColor = Color.White
        ) {
            when (currentSheet) {
                SheetContent.MESSAGE -> MessageSheet(
                    post = post,
                    viewModel = viewModel,
                    currentUserId = currentUserId,
                    onConfirm = { showBottomSheet = false }
                )
                SheetContent.EDIT -> EditSheet(
                    post = post,                      // 傳入必要參數
                    viewModel = viewModel,
                    navController = navController,
                    onConfirm = { showBottomSheet = false }
                )
                SheetContent.NONE -> { /* No content to display */ }
            }
        }
    }
}

@Composable
fun PostDetailItem(
    post: Post,
    currentUserId: Int,  // 添加當前用戶ID參數
    onEditClick: () -> Unit,
    onMessageClick: () -> Unit,
    onUserClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // User info section
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

            Column(modifier = Modifier.weight(1f)) {
                Text(text = post.publisher.name)
                Text(text = post.location)
            }

            // 只有當前用戶是發布者時才顯示More options按鈕
            if (currentUserId == post.publisher.id) {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display images
        ImageDisplay(imageSource = ImageSource.CarouselSource(post.carouselItems))

        Spacer(modifier = Modifier.height(8.dp))

        // Action row with chat bubble
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
                FavoriteIcon()

                IconButton(onClick = onMessageClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat_bubble_outline_24),
                        contentDescription = "Chat Bubble",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Text(
            text = post.content,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
fun MessageSheet(
    post: Post,
    viewModel: PostViewModel,
    currentUserId: Int,
    onConfirm: () -> Unit,
) {
    var commentText by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val currentPost by viewModel.getPostById(post.postId).collectAsState()
    val comments = currentPost?.comments ?: post.comments

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "留言",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(comments) { comment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(
                        imageData = comment.commenter.avatarBitmap,
                        defaultImage = comment.commenter.avatarImage,
                        contentDescription = "Avatar for ${comment.commenter.name}"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = comment.commenter.name, fontWeight = FontWeight.SemiBold)
                        Text(text = comment.content)
                    }
                }
            }
        }

        // 在輸入框之前添加空間
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            enabled = !isLoading,
            placeholder = { Text(text = stringResource(id = R.string.Add_message)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp), // 增加底部 padding
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            viewModel.createComment(
                                postId = post.postId,
                                userId = currentUserId,
                                content = commentText
                            )
                            commentText = ""
                        }
                    },
                    enabled = !isLoading && commentText.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_send_24),
                            contentDescription = "Send Comment",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
@Composable
fun EditSheet(
    post: Post,
    viewModel: PostViewModel,
    navController: NavHostController,
    onConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight(0.4f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = {
                navController.navigate("${context.getString(R.string.str_post)}/edit/${post.postId}")
                onConfirm()
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, colorResource(id = R.color.orange_1st)),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.edit),
                color = colorResource(id = R.color.orange_1st)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedButton(
            onClick = { showDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, colorResource(id = R.color.orange_1st)),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.delete),
                color = colorResource(id = R.color.orange_1st)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "確定要刪除？",
                    color = colorResource(id = R.color.black)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            showDialog = false
                            viewModel.deletePost(context, post.postId)
                        }
                        onConfirm()  // 關閉 BottomSheet
                        navController.navigate(context.getString(R.string.str_Recommended_posts))
                    }
                ) {
                    Text(
                        text = "刪除",
                        color = colorResource(id = R.color.orange_1st)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(
                        text = "取消",
                        color = colorResource(id = R.color.black)
                    )
                }
            },
            containerColor = Color.White
        )
    }
}