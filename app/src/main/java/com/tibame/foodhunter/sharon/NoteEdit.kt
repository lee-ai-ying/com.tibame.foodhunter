package com.tibame.foodhunter.sharon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddNotePreview() {
    val mockNavController = rememberNavController()
    EditNote(mockNavController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNote(
    navController: NavHostController = rememberNavController(), // 這裡創建或接收 NavController，用於控制導航
    isShow: Boolean = false,

    ) {
    // 初始化 scrollBehavior
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // 定義狀態
    var titleInputText by remember { mutableStateOf("") }
    var bodyInputText by remember { mutableStateOf("") }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedRestaurantName by remember { mutableStateOf("") }

    // 定義樣式
    val editNoteBasicTextStyle = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight(500),
        color = Color(0xFF9A9FAE),
    )

    val editNoteTitleTextStyle = editNoteBasicTextStyle.copy(
        fontSize = 30.sp,
        fontWeight = FontWeight(500),
    )

    val editNoteBodyTextStyle = editNoteBasicTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight(400)
    )

// Scaffold 組件，包含 TopAppBar 和內容區
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NoteTopBar(
                canback = true,
                navController = navController,
                scrollBehavior = scrollBehavior,
                hasTitleInput = titleInputText.isNotEmpty()
            )
        }
    ) { innerPadding ->
        // 使用 innerPadding 為內容設定邊距
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 34.dp,
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 34.dp,
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 12.dp
                ),
//                .padding(horizontal = 34.dp, vertical = 12.dp,top),
//                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        )
        {
            Box(  // 輸入標題
                modifier = Modifier
                    .height(44.dp)
                    .padding(bottom = 2.dp), // 設置 Box 的最小高度
                contentAlignment = Alignment.TopStart // 將所有內容左下對齊
            ) {
                BasicTextField(
                    value = titleInputText,
                    onValueChange = {
                        if (it.length <= 10) {
                            titleInputText = it
                        }
                    },
                    textStyle = editNoteTitleTextStyle,
                )
                if (titleInputText.isEmpty()) {
                    Text(
                        text = "請輸入標題",
                        style = editNoteTitleTextStyle,
                    )
                }
            }
            Row(
                // 顯示餐廳、顯示日期
                modifier = Modifier
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),            ) {
                // 顯示餐廳名稱
                DisplayRestaurantChip(
                    label = selectedRestaurantName,
                    onClear = { selectedRestaurantName = "" }
                )
                VerticalLine()
                // 顯示日期
                DisplayDateChip()
            }

            Box(  // 輸入內文
                modifier = Modifier
                    .heightIn(min = 24.dp)
            ) {
                BasicTextField(
                    value = bodyInputText,
                    onValueChange = {
                        if (it.length <= 500) {
                            bodyInputText = it
                        }
                    },
                    textStyle = editNoteBodyTextStyle,
                )
                if (bodyInputText.isEmpty()) {
                    Text(
                        text = "請輸入內文",
                        style = editNoteBodyTextStyle,
                    )
                }
            }

            // 選擇 bottomSheet 的 chip
            SelectRestaurantChip(
                selectedRestaurant = true,
                onClick = {
                    isBottomSheetVisible = true
                }
            )
// 控制 BottomSheet 顯示: 關閉 BottomSheet，控制內部onClose
            if (isBottomSheetVisible) {
                SelectRestaurantBottomSheet(
                    onRestaurantPicked = { restaurant ->
                        selectedRestaurantName = restaurant  // 更新選定的餐廳名稱
                        isBottomSheetVisible = false // 統一關閉 BottomSheet
                    },
                    onClose = { isBottomSheetVisible = false } // 統一關閉 BottomSheet
                )
            }

        }
        }
}

@Composable
fun VerticalLine() {
    Box(
        modifier = Modifier
            .width(8.dp)
            .height(20.dp)
//            .padding(start = 0.dp, end = 4.dp)

    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight() // 讓 Box 的高度填滿可用空間（例如 Row 或 Column 的高度）
                .background(Color.Gray)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun DisplayDateChip(
//    isVisible: Boolean,
) {
    var selected by remember { mutableStateOf(true) }

    FilterChip(
        modifier = Modifier
            .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
            .height(32.dp),
        // TODO(更改日期: 串接日曆、把參數帶入)
        onClick = {},
        label = {
            Text(
                text = "2024-10-12", modifier = Modifier
            )
        },
        selected = selected,
    )
}

@Composable
fun SelectRestaurantChip(
    onClick: () -> Unit,
    selectedRestaurant: Boolean = false,
) {
    FilterChip(
        // 選擇餐廳時，點擊 > bottom sheet > bottom sheet選擇完畢 > Show標籤函數
        onClick = onClick,
        // 有選擇到餐廳，為選取狀態
        selected = selectedRestaurant,
        label = {
            if (selectedRestaurant) {
                Text("選擇餐廳")
            } else {
                Text("  ")

            }
        },
    )
}


@Composable
fun DisplayRestaurantChip(
    label: String,
    onClear: () -> Unit
) {
    // 只有當 label 非空時才渲染 FilterChip
    if (label.isNotEmpty()) {
        FilterChip(
            onClick = {},
            selected = true,
            label = {
                // TODO: 點擊後跳轉到餐廳資訊頁面
                Text(label)
            },
            enabled = true,
            trailingIcon = {
                IconButton(
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                    onClick = onClear
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRestaurantBottomSheet(
    onRestaurantPicked: (String) -> Unit,
    onClose: () -> Unit // 關閉 BottomSheet 的回調
) {
    // 開滿頁面或開一半
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    // 預覽顯示用，但尚未測試成功
    val standardSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded
    )
    val coroutineScope = rememberCoroutineScope()


// chip 點選後 出現Bottom sheet
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = modalSheetState,
        onDismissRequest = {
            coroutineScope.launch { modalSheetState.hide() }
            onClose() // 關閉 BottomSheet，在外部控制
       },
        scrimColor = Color.Black.copy(alpha = 0.5f), // 半透明灰色背景
        properties = ModalBottomSheetProperties(
            isFocusable = true,  // 允許接收焦點，例如接收TextFiled輸入事件
            shouldDismissOnBackPress = true,
            securePolicy = SecureFlagPolicy.SecureOff, // 關閉，防止應用窗口的內容被截圖或錄屏
        ),
        content = {
            BottomSheetContent(
                onRestaurantPicked = { restaurant ->
                    onRestaurantPicked(restaurant) // 傳回選定餐廳
                    coroutineScope.launch { modalSheetState.hide() }
                    onClose() // 關閉 BottomSheet，在外部控制

                },
                onClose = {
                    coroutineScope.launch { modalSheetState.hide() }
                    onClose() // 關閉 BottomSheet，在外部控制
                }
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    onClose: () -> Unit, // 設定 Bottom Sheet 關閉的回調參數
    onRestaurantPicked: (String) -> Unit // 餐廳資訊回調

) {
    // 頂部工具欄
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .background(Color.Blue),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "選擇餐廳",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.alpha(0f) // 設置透明度
            ) {
                Icon(Icons.Filled.Check, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = { onClose() }) {
                Icon(Icons.Filled.Close, contentDescription = "")
            }
        }
    )

    // 水平分隔線
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp
    )

    // 餐廳選擇按鈕
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Blue)
    ) {
        // 餐廳選擇按鈕，選擇餐廳後執行回調並關閉 BottomSheet
        Button(onClick = {
            onRestaurantPicked("肯德基") // 選擇餐廳
            onClose() // 關閉 BottomSheet
        }) {
            Text("我是肯德基")
        }

    // TODO(接入餐廳)
//        RestaurantSelectionScreen { selectedRestaurant ->
//            onRestaurantPicked(selectedRestaurant)
//            onClose()
//        }

    }
}
