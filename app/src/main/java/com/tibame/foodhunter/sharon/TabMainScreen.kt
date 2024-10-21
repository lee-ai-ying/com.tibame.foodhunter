package com.tibame.foodhunter.sharon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tibame.foodhunter.R

import com.tibame.foodhunter.sharon.*

@Composable
fun TabMainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabList = listOf(
        stringResource(id = R.string.str_calendar),
        stringResource(id = R.string.str_note),
        stringResource(id = R.string.str_favorite)
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.LightGray)) {
        // 顯示 Tab 列表
        NiaTabRow(selectedTabIndex = selectedTab) {
            tabList.forEachIndexed { index, title ->
                NiaTab(
                    text = { Text(text = title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        // 根據選中的 Tab 顯示對應的頁面
        when (selectedTab) {
            0 -> CalendarScreen()
            1 -> NoteScreen()
            2 -> FavoriteScreen()
        }
    }
}


@Preview
@Composable
fun TabPreview() {
    TabMainScreen()
}