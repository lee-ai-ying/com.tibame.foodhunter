package com.tibame.foodhunter.a871208s

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendManagementScreen(navController: NavHostController = rememberNavController(),friendVM: FriendViewModel = viewModel()) {
    val context = LocalContext.current

// 設定內容向上捲動時，TopAppBar自動收起來；呼叫pinnedScrollBehavior()則不會收起來
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val frined by friendVM.FriendState.collectAsState()
    Column(
    ) {
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
                    .padding(10.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "date",
                    modifier = Modifier.clickable {
                        navController.navigate(context.getString(R.string.str_member) + "/7")
                    })
            }
        }
        HorizontalDivider(
            modifier = Modifier.size(500.dp, 1.dp),
            color = Color.Blue
        )
        Text(
            text = "已追蹤",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        HorizontalDivider(
            modifier = Modifier.size(500.dp, 1.dp),
            color = Color.Blue
        )
        Scaffold(
            modifier = Modifier.fillMaxWidth(),

            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            // 一定要套用innerPadding，否則內容無法跟TopAppBar對齊
            FriendLists(frined , innerPadding) { friend ->
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "${friend.name}",
                        withDismissAction = true
                    )
                }
            }
        }
        Text(
            text = "建議追蹤",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        HorizontalDivider(
            modifier = Modifier.size(500.dp, 1.dp),
            color = Color.Blue
        )
    }

}

/**
 * 列表內容
 * @param friends 欲顯示的書籍清單
 * @param innerPadding 元件要套用innerPadding，否則內容無法跟TopAppBar對齊
 * @param onItemClick 點擊列表項目時所需呼叫的函式
 */
@Composable
fun FriendLists(
    friends: List<Friend>,
    innerPadding: PaddingValues,
    onItemClick: (Friend) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "確定要取消追蹤?") },
            confirmButton = {
                Button(
                    onClick = {}  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("確定")
                }
                Button(
                    onClick = { showDialog = false }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("取消")
                }
            }
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        items(friends) { friend ->
            // 用來建立Lists內容物
            ListItem(
                modifier = Modifier.clickable {
                    onItemClick(friend)
                },
                headlineContent = { Text(friend.name) },
                leadingContent = {

                        Image(
                            painter = painterResource(id = friend.image),
                            contentDescription = "friend",
                            modifier = Modifier.size(30.dp)
                                .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                },
                        trailingContent = {

                        Row {

                            // 刪除按鈕
                            IconButton(onClick = {showDialog = true}) {
                                Icon(Icons.Filled.Delete, contentDescription = "delete")
                            }

                    }
                }
            )
            HorizontalDivider()
        }
    }

}


/**
 * 載入測試需要資料
 * @return 多本書資訊
 */


@Preview(showBackground = true)
@Composable
fun FriendManagementScreenPreview() {
    MaterialTheme {
        FriendManagementScreen(rememberNavController())
    }

}