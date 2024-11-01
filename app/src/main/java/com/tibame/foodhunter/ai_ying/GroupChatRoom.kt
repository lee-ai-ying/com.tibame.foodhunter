package com.tibame.foodhunter.ai_ying

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun GroupChatRoom(
    groupRoomId: Int,
    gChatVM: GroupViewModel
) {
    gChatVM.gotoChatRoom(groupRoomId)
//    if (gChatVM.showEditGroup.asStateFlow().collectAsState().value) {
//        EditGroupInformation(gChatVM)
//        return
//    }
    if (gChatVM.showEditMember.asStateFlow().collectAsState().value) {
        EditGroupMember(gChatVM)
        return
    }
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
    val chatRoom = gChatVM.chatRoom.collectAsState().value
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = chatRoom.name,
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupChatRoomBottomBar(
    navController: NavHostController,
    gChatVM: GroupViewModel
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )
    val chatInput = gChatVM.chatInput.collectAsState()
    var showFunc by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var showEndAlert by remember { mutableStateOf(false) }
    var showNotManagerAlert by remember { mutableStateOf(false) }
    var showManagerFunction by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var stars by remember { mutableIntStateOf(0) }
    val showEditGroup=gChatVM.showEditGroup.asStateFlow().collectAsState().value
    val showEditMember=gChatVM.showEditMember.asStateFlow().collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        if (!showEditGroup && !showEditMember){
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
                        showFunc = !showFunc
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
            if (showFunc){
                Spacer(Modifier.height(16.dp))
            }
        }
        if (showFunc) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (!showManagerFunction) {
                        GroupFunction("傳送圖片", Icons.Filled.AccountBox) {
                            pickImageLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        GroupFunction("發表評論", Icons.Filled.Edit) {
                            showBottomSheet = true
                        }
                        GroupFunction("離開揪團", Icons.AutoMirrored.Filled.ExitToApp) {
                            showAlert = true
                        }
                        GroupFunction("管理成員", Icons.Outlined.Person) {
                            if (true) {
                                gChatVM.setShowEditMember(true)
                            } else {
                                showNotManagerAlert = true
                            }
                        }
                        GroupFunction("終止揪團", Icons.Filled.Settings) {
                            if (true) {
                                showEndAlert = true
                            } else {
                                showNotManagerAlert = true
                            }
                        }
                    }
//                    else {
//                        GroupFunction("返回上層", Icons.AutoMirrored.Filled.ArrowBack) {
//                            showManagerFunction = false
//                            gChatVM.setShowEditGroup(false)
//                            gChatVM.setShowEditMember(false)
//                        }
//                        GroupFunction("修改揪團", Icons.Filled.Create) {
//                            gChatVM.setShowEditGroup(true)
//                        }
//                        GroupFunction("管理成員", Icons.Outlined.Person) {
//                            gChatVM.setShowEditMember(true)
//                        }
//                    }
                }
            }
        }
        if (showNotManagerAlert) {
            NotManagerDialog(
                onDismissRequest = {
                    showNotManagerAlert = false
                },
                gChatVM = gChatVM
            )
        }
        if (showEndAlert) {
            EndGroupChatDialog(
                // 點擊對話視窗外部或 back 按鈕時呼叫
                onDismissRequest = {
                    showEndAlert = false
                },
                // 設定確定時欲執行內容
                onConfirm = {
                    navController.popBackStack()
                    showEndAlert = false
                },
                // 設定取消時欲執行內容
                onDismiss = {
                    showEndAlert = false
                },
                gChatVM = gChatVM
            )
        }
        if (showAlert) {
            LeaveGroupChatDialog(
                // 點擊對話視窗外部或 back 按鈕時呼叫
                onDismissRequest = {
                    showAlert = false
                },
                // 設定確定時欲執行內容
                onConfirm = {
                    navController.popBackStack()
                    showAlert = false
                },
                // 設定取消時欲執行內容
                onDismiss = {
                    showAlert = false
                },
                gChatVM = gChatVM
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                // 使用者點擊bottom sheet以外區域時執行
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "發表評論")
                    Text(text = "XXX餐廳")
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        repeat(stars) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "",
                                modifier = Modifier.clickable {
                                    stars = it + 1
                                }
                            )
                        }
                        repeat(5 - stars) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "",
                                modifier = Modifier.clickable {
                                    stars += it + 1
                                }
                            )
                        }
                    }
                    GroupTextInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        singleLine = false,
                        maxLines = 5,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,//colorResource(R.color.orange_4th),
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer//colorResource(R.color.orange_4th)
                        ),
                        onValueChange = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Button(
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text("取消", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Button(
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            },
                        ) {
                            Text("送出")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

    }
}
//@Composable
//fun EditGroupInformation(
//    gChatVM: GroupViewModel
//){
//    val chatRoom = gChatVM.chatRoom.collectAsState().value
//    var showDatePickerDialog by remember { mutableStateOf(false) }
//    Column{
//        GroupTitleText(text = stringResource(R.string.str_create_group))
//        Column (
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )
//        {
//            GroupText(text = stringResource(R.string.str_create_name))
//            GroupSingleInput {
//                chatRoom.name = it
//            }
//            GroupText(text = stringResource(R.string.str_create_location))
//            GroupSingleWithIcon(
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Outlined.Place,
//                        contentDescription = ""
//                    )
//                }
//            ) {
//                chatRoom.location = it
//            }
//            GroupText(text = stringResource(R.string.str_create_time))
//            GroupSingleInputWithIcon(
//                placeholder = {
//                    Text(chatRoom.time)
//                },
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Outlined.DateRange,
//                        contentDescription = "",
//                        modifier = Modifier.clickable {
//                            showDatePickerDialog = true
//                        }
//                    )
//                }
//            ) {
//                chatRoom.time = it
//            }
//            GroupText(text = stringResource(R.string.str_create_price))
//            GroupPriceSlider {
//                chatRoom.price = it
//            }
//            GroupText(text = stringResource(R.string.str_create_public))
//            GroupDropDownMenu(listOf("public", "invite", "private")) {
//                chatRoom.public = it
//            }
//            GroupText(text = stringResource(R.string.str_create_describe))
//            GroupBigInput(5) {
//                chatRoom.describe = it
//            }
//            //*/
//            Spacer(modifier = Modifier.size(8.dp))
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Button(
//                    onClick = {
//
//                    },
//                ) {
//                    Text("確定")
//                }
//            }
//            Spacer(modifier = Modifier.size(8.dp))
//        }
//    }
//}
@Composable
fun EditGroupMember(
    gChatVM: GroupViewModel
){
    val users = listOf(1,2,3,4,5)
    val users2 = listOf(1,2,3,4,5,6,7,8,9,10)
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        items(1){
            GroupTitleText(text = "待確認")
        }
        items(users){
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .clickable {
                    },
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
                            painter = painterResource(id = R.drawable.user1),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "$it"
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = ""
                )
            }
        }
        items(1){
            GroupTitleText(text = "已加入")
        }
        items(users2){
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .clickable {
                    },
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
                            painter = painterResource(id = R.drawable.user1),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "$it"
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = ""
                )
            }
        }
    }
}
@Composable
fun EndGroupChatDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    gChatVM: GroupViewModel
) {
    val chatRoom = gChatVM.chatRoom.collectAsState()
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("確定要終止此揪團嗎?") },
        text = { Text("警告：此動作無法復原") },
        // 設定確定按鈕
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text("確定")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text("取消", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    )
}
@Composable
fun LeaveGroupChatDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    gChatVM: GroupViewModel
) {
    val chatRoom = gChatVM.chatRoom.collectAsState()
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("確定要離開此揪團嗎?") },
        text = { Text("警告：有可能無法再加入${chatRoom.value.name}") },
        // 設定確定按鈕
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text("確定")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text("取消", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    )
}

@Composable
fun NotManagerDialog(
    onDismissRequest: () -> Unit,
    gChatVM: GroupViewModel
) {
    val chatRoom = gChatVM.chatRoom.collectAsState()
    Dialog(onDismissRequest = onDismissRequest) {
        // card適合用在dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            // 形狀設定為圓角矩形
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "你不是的${chatRoom.value.name}管理員!",
                    modifier = Modifier.padding(8.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("確定")
                }

            }
        }
    }
}

@Composable
fun GroupFunction(
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            //.fillMaxWidth()//fillMaxWidth(0.22f)
//            .padding(8.dp)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GroupChatRoomPreview() {
    MaterialTheme {
//        GroupChatRoom(0, viewModel())
//        GroupChatRoomBottomBar(rememberNavController(), viewModel())
        EditGroupMember(viewModel())
    }
}