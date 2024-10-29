package com.tibame.foodhunter.zoe

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import kotlinx.coroutines.launch

// 定義內容類型
enum class BottomSheetContentType {
    TYPE_ONE, TYPE_TWO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoContentBottomSheetDemo() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var sheetContentType by remember { mutableStateOf(BottomSheetContentType.TYPE_ONE) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            when (sheetContentType) {
                BottomSheetContentType.TYPE_ONE -> ContentOne()
                BottomSheetContentType.TYPE_TWO -> ContentTwo()
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        // 主頁面內容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = {
                sheetContentType = BottomSheetContentType.TYPE_ONE
                coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
            }) {
                Text("顯示內容一")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                sheetContentType = BottomSheetContentType.TYPE_TWO
                coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
            }) {
                Text("顯示內容二")
            }
        }
    }
}

// 底部內容一
@Composable
fun ContentOne() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("這是內容一", color = Color.Blue)
        Spacer(modifier = Modifier.height(8.dp))
        Text("這是內容一的詳細說明。")
    }
}

// 底部內容二
@Composable
fun ContentTwo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("這是內容二", color = Color.Green)
        Spacer(modifier = Modifier.height(8.dp))
        Text("這是內容二的詳細說明。")
    }
}


@Preview(showBackground = true)
@Composable
fun TestpPreview() {
    FoodHunterTheme  {
        TwoContentBottomSheetDemo()
    }



}
