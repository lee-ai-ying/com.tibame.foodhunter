package com.tibame.foodhunter.sharon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.checkTopBarBackButtonShow
import com.tibame.foodhunter.checkTopBarNoShow
import com.tibame.foodhunter.global.TopFunctionBar
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ofLocalizedDate
import java.time.format.FormatStyle

/** 假裝這是會員功能準備進到-->日曆的入口點 **/
@Composable
fun CalendarScreen(
    navController: NavHostController? = null,
    callback: @Composable () -> Unit // 接收一個可組合的回調函數，用於在頁面中展示額外的 UI
) {
    // 使用 LazyColumn 以支持垂直滾動
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
//            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // DatePicker 組件
        item {
            CalendarComp()
        }
        // CalendarGroupItem 卡片
        item {
            CalendarGroupItem()
        }
        // NoteCardItem 卡片
        item {
            NoteCardItem(title = "小巷中的咖啡廳")
        }
    }
}



// 使用的DatePicker屬於androidx.compose.material3測試功能，需要加上"@OptIn"註記
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarComp() {

    val today = LocalDate.now()
    // 使用 atStartOfDay 將當前日期轉換為開始時間（00:00），然後轉換成 UTC 時區並轉換成毫秒（Epoch milliseconds）
    val todayInMills = today.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

    // 將 todayInMillis 作為初始選取日期傳入 rememberDatePickerState
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayInMills  // 設定初始值為今天日期
            )


    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DatePicker(
//            modifier = Modifier.,
            state = datePickerState,
//            title = { Text(text = "") },
            headline = {
                DatePickerDefaults.DatePickerTitle(
                    displayMode = datePickerState.displayMode,
                    modifier = Modifier.padding(top = 0.dp) // 使用自定義的 titlePadding
                )
                       },
            title = {
                DatePickerDefaults.DatePickerTitle(
                    displayMode = datePickerState.displayMode,
                    modifier = Modifier.padding(top = 0.dp) // 使用自定義的 titlePadding
                )
            },
            colors = DatePickerDefaults.colors(

                todayContentColor = Color.Black,  // 今天日期字的顏色
                todayDateBorderColor = Color.Red,  // 今天日期的邊緣線
                selectedDayContainerColor = Color.LightGray,  // 選中日期的背景色
                selectedDayContentColor = Color.Black,  // 選中日期的字顏色
            )

        )
        // 顯示選取日期(已格式化)
//        Text(
//            text = "Selected date: ${
//                datePickerState.selectedDateMillis?.let {
//                    Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
//                        .toLocalDate().format(ofLocalizedDate(FormatStyle.MEDIUM))
//                } ?: "no selection"
//            }"
//        )
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FoodHunterTheme {

        CalendarScreen {}
    }
}