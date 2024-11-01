package com.tibame.foodhunter.zoe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
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
    postViewModel: PostViewModel = viewModel()
) {
    // 根據 postId 從 ViewModel 中取得特定的貼文
    val post = postId?.let { postViewModel.getPostById(it).collectAsState().value }

    post?.let { nonNullPost ->
        // 顯示貼文詳細內容
        PostDetail(post = nonNullPost)
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
        Button(onClick = { navController.popBackStack() }) {
            Text("返回")
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun PostDetail(post: Post) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var currentSheet by remember { mutableStateOf(SheetContent.NONE) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            when (currentSheet) {
                SheetContent.MESSAGE -> MessageSheet(post = post)
                SheetContent.EDIT -> EditSheet()
                SheetContent.NONE -> {
                    // 空的內容，保持 BottomSheet 關閉
                }
            }
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PostDetailItem(
                post = post,
                onEditClick = {
                    currentSheet = SheetContent.EDIT
                    coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                },
                onMessageClick = {
                    currentSheet = SheetContent.MESSAGE
                    coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailItem(post: Post,
                   onEditClick: () -> Unit,
                   onMessageClick: () -> Unit) {

    val sheetState = rememberModalBottomSheetState()
    var currentSheet by remember { mutableStateOf(SheetContent.NONE) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        // User info section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = post.publisher.avatarImage),
                contentDescription = "Publisher avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )

            Column ( modifier = Modifier.weight(1f)){
                Text(text = post.publisher.name)
                Text(text = post.location)
            }



            IconButton(onClick =onEditClick) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
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

            Icon(
                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                contentDescription = "Bookmark",
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = post.content,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (currentSheet != SheetContent.NONE) {
        ModalBottomSheet(
            onDismissRequest = { currentSheet = SheetContent.NONE },
            sheetState = sheetState,
        ) {
            when (currentSheet) {
                SheetContent.MESSAGE -> MessageSheet(post = post)
                SheetContent.EDIT -> EditSheet()
                SheetContent.NONE -> { /* 不需要做任何事 */ }
            }
        }
    }
}

@Composable
fun MessageSheet(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Add title
        Text(
            text = "留言",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f), // allows LazyColumn to take available height
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(post.comments) { comment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = comment.commenter.avatarImage),
                        contentDescription = "Commenter avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = comment.commenter.name, fontWeight = FontWeight.SemiBold)
                        Text(text = comment.content)
                    }
                }
            }
        }

        // Input section with rounded corners
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle input */ },
            placeholder = { Text(text = stringResource(id = R.string.Add_message)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = { /* Handle send comment */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_send_24),
                        contentDescription = "Send Comment",
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            shape = RoundedCornerShape(16.dp) // Increased roundness for a more rounded look
        )
    }
}


@Composable

fun EditSheet(

) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight(0.4f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = {
                // 編輯按鈕點擊事件
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent // 背景透明
            ),
            border = BorderStroke(1.dp, colorResource(id = R.color.orange_1st)), // 邊框設置為橘色
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.edit),
                color = colorResource(id = R.color.orange_1st) // 文字顏色設置為橘色
            )
        }

        Spacer(modifier = Modifier.height(22.dp)) // 兩個按鈕之間的間距

        OutlinedButton(
            onClick = {
                showDialog = true
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent // 背景透明
            ),
            border = BorderStroke(1.dp, colorResource(id = R.color.orange_1st)), // 邊框設置為橘色
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.delete),
                color = colorResource(id = R.color.orange_1st) // 文字顏色設置為橘色
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "確定要刪除？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 執行刪除邏輯
                        showDialog = false
                    }
                ) {
                    Text(text = "刪除", color = colorResource(id = R.color.orange_1st))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "取消")
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GroupChatRoomPreview() {
    MaterialTheme {

        PostDetailScreen(1, rememberNavController())
    }
}
