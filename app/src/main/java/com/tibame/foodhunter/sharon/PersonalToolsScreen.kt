package com.tibame.foodhunter.sharon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalToolsScreen(
    navController: NavHostController,
    initTab: Int,
) {
    // 獲取當前導航棧中的目的地，用於判斷是否顯示 TopBar 和返回按鈕
    val destination = navController.currentBackStackEntryAsState().value?.destination
    // 當前選到的Tab
    var selectedTab by remember { mutableIntStateOf(initTab) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    Scaffold(
        modifier = Modifier
            .fillMaxSize()  // 確保填滿
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = Color.White),
        floatingActionButton = {
            when (initTab) {
                in TabConstants.CALENDAR..TabConstants.NOTE  ->
                    FloatingActionButton(
                        containerColor = colorResource(R.color.orange_1st),
                        contentColor = colorResource(R.color.white) ,
                        modifier = Modifier.padding(start =317.dp, bottom = 76.dp),
                        onClick = { print("") },
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Add")
                    }

                TabConstants.FAVORITE -> {}
            }
        },
    ) { innerPadding -> // innerPadding 是 Scaffold 自動提供的內邊距，通常會包括 TopBar、BottomBar 的高度
        Column(
            modifier = Modifier
                // 只使用底部的 padding，忽略頂部的
                .padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    // top = 0.dp  // 頂部不需要 padding
                )
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            // 頁籤切換
            TabBarComponent(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabList = listOf(
                    stringResource(id = R.string.str_calendar),
                    stringResource(id = R.string.str_note),
                    stringResource(id = R.string.str_favorite)
                )
            )

            // 根據選中的 Tab 顯示對應的頁面
            when (selectedTab) {
                TabConstants.CALENDAR -> CalendarScreen(navController)
                TabConstants.NOTE -> NoteScreen(navController)
                TabConstants.FAVORITE -> FavoriteScreen(navController)
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
        PersonalToolsScreen(navController = mockNavController, 0)
    }
}

