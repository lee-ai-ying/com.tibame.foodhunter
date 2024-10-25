package com.tibame.foodhunter.sharon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
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
//    callback: @Composable () -> Unit, // 接收一個可組合的回調函數，用於在頁面中展示額外的 UI
    placeholderText: String = "請輸入",
    isShow: Boolean = false,

) {
    var inputText by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val focusRequester = remember { FocusRequester() }
    var selectRestaurantShow by remember { mutableStateOf(false) }


//    LaunchedEffect(Unit) {  // 預覽模式的Button sheet會打不開
//        focusRequester.requestFocus()
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp,32.dp)
    ) {
        TextField(
            // TODO(Basic建立無內間距的輸入)
            modifier = Modifier
                .fillMaxWidth(),
            value = inputText,
            // 輸入的值改變時呼叫此方法
            onValueChange = { inputText = it },
            placeholder = { Text(
                text = placeholderText,
                style = TextStyle(
                    fontSize = 20.sp, // 字體大小
                    fontWeight = null, // 字體粗細
                    fontFamily = null, // 字體樣式
                    color = Color.LightGray
                ),
            ) },
            textStyle = TextStyle(textAlign = TextAlign.Start),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // 焦點時容器背景透明
                unfocusedContainerColor = Color.Transparent, // 未焦點時容器背景透明
                disabledContainerColor = Color.Transparent,  // 禁用時容器背景透明
                errorContainerColor = Color.Transparent,     // 錯誤狀態下容器背景透明
                focusedIndicatorColor = Color.Transparent,   // 焦點框線透明
                unfocusedIndicatorColor = Color.Transparent, // 未焦點框線透明
                disabledIndicatorColor = Color.Transparent,  // 禁用狀態下框線透明
                errorIndicatorColor = Color.Transparent      // 錯誤狀態下框線透明
            ),
        )
        Button(
            onClick = { selectRestaurantShow = true }
        ) {
            Text("show label")
        }
        SelectRestaurantChip(isVisible = selectRestaurantShow)
//        Text(
//            text = "$phone",
//            modifier = Modifier.padding(16.dp)
//        )

        TextField(
            // TODO(Basic建立無內間距的輸入)
            modifier = Modifier
                .fillMaxWidth(),
            value = inputText,
            // 輸入的值改變時呼叫此方法
            onValueChange = { inputText = it },
            placeholder = { Text(
                text = "請輸入內文",
                style = TextStyle(
                    fontSize = 20.sp, // 字體大小
                    fontWeight = FontWeight.Bold, // 字體粗細
//                    fontFamily = null, // 字體樣式
                    color = Color.LightGray
                ),
            ) },
            textStyle = TextStyle(textAlign = TextAlign.Start),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // 焦點時容器背景透明
                unfocusedContainerColor = Color.Transparent, // 未焦點時容器背景透明
                disabledContainerColor = Color.Transparent,  // 禁用時容器背景透明
                errorContainerColor = Color.Transparent,     // 錯誤狀態下容器背景透明
                focusedIndicatorColor = Color.Transparent,   // 焦點框線透明
                unfocusedIndicatorColor = Color.Transparent, // 未焦點框線透明
                disabledIndicatorColor = Color.Transparent,  // 禁用狀態下框線透明
                errorIndicatorColor = Color.Transparent      // 錯誤狀態下框線透明
            ),
        )
    }
}

@Composable
fun SelectRestaurantChip(
    isVisible: Boolean
) {
    var selected by remember { mutableStateOf(false) }
    PartialBottomSheet1(isVisible)
    if (isVisible) {
    FilterChip(
        onClick = {},
        label = {
            if (selected) {
                Text("已選取")
            } else {
                Text("麥當勞 南京復興店") // 預設空白
            }
        },
        selected = selected,
        trailingIcon = if (selected) {
            {
                IconButton(
                modifier = Modifier.size(FilterChipDefaults.IconSize),
                onClick = { selected = !selected }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            }
        } else {
            null
        },
    )
}}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet1(isVisible:Boolean = true) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val scope = rememberCoroutineScope()
    var isVisible1 by remember { mutableStateOf(isVisible) }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { showBottomSheet = true }
        ) {
            Text("Display partial bottom sheet")
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                // TODO 引入餐廳
                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End // 使內容向右對齊
                ) {
                    Text(
                        "Swipe up to open sheet. Swipe down to dismiss.",
                        modifier = Modifier.padding(16.dp),
//                        horizontalArrangement = Arrangement.End // 使內容向右對齊

                    )
                    IconButton(
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        })
                     {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            }
        }
    }
}