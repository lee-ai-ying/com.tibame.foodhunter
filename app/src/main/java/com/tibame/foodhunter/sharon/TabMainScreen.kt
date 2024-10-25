package com.tibame.foodhunter.sharon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.checkTopBarBackButtonShow
import com.tibame.foodhunter.checkTopBarNoShow
import com.tibame.foodhunter.global.TopFunctionBar
import com.tibame.foodhunter.ui.theme.FoodHunterTheme

/** 假裝這是會員功能準備進到-->日曆的入口點 **/
@Composable
fun MemberScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column (modifier = Modifier.fillMaxSize()){
        Text("這是會員功能")
        Button(
            onClick = {
                navController.navigate(context.getString(R.string.str_calendar))
            }
        ) { Text(text = stringResource(id = R.string.str_calendar)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMainScreen(navController: NavHostController,initTab: Int) {
    // 獲取當前導航棧中的目的地，用於判斷是否顯示 TopBar 和返回按鈕
    val destination = navController.currentBackStackEntryAsState().value?.destination
    // 當前選到的Tab
    var selectedTab by remember { mutableIntStateOf(initTab) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    // Scaffold 結構是整個頁面的骨架，包含 TopBar、BottomBar、內容等
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    InsideTopBar(true, navController, scrollBehavior)
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.padding(39.dp,192.dp),
                        onClick = {print("")},
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }

        ) { innerPadding -> // innerPadding 是 Scaffold 自動提供的內邊距，通常會包括 TopBar、BottomBar 的高度
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = Color.LightGray)
            ) {
                // 頁籤切換
                TabBarComponent(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // 根據選中的 Tab 顯示對應的頁面
                when (selectedTab) {
                    0 -> CalendarScreen(navController) {}
                    1 -> NoteScreen(navController)
                    2 -> FavoriteScreen(navController)
                }
            }
        }
    }


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TabMainScreenPreview() {
    // 使用 rememberNavController() 創建一個模擬的 NavController
    val mockNavController = rememberNavController()
    FoodHunterTheme {
        // 調用你要預覽的 UI 函數
        TabMainScreen(navController = mockNavController, 0)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MemberScreenPreview() {
//    // 使用 rememberNavController() 創建一個模擬的 NavController
//    val mockNavController = rememberNavController()
//
//    // 調用你要預覽的 UI 函數
//    MemberScreen(navController = mockNavController)
//}