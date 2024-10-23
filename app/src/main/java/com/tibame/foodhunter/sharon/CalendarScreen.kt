package com.tibame.foodhunter.sharon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    navController: NavHostController = rememberNavController(), // 這裡創建或接收 NavController，用於控制導航
    callback: @Composable () -> Unit // 接收一個可組合的回調函數，用於在頁面中展示額外的 UI
) {
    val context = LocalContext.current // 獲取當前的 LocalContext，通常用於獲取資源或執行導航
    var i = "ok"
    Column {
        
        CalendarComp()
    }
}


// 使用的DatePicker屬於androidx.compose.material3測試功能，需要加上"@OptIn"註記
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarComp() {

    val today = LocalDate.now()
    val datePickerState = rememberDatePickerState()

////
//    val datePickerState = rememberDatePickerState(
//        // SelectableDates介面用來限制可選擇的日期與年，
//        selectableDates = object : SelectableDates {
//            // 週六日不可選擇
//            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//                /* 將使用者選取的時間轉成LocalDate物件後取出星期幾的資訊
//                   API 26開始支援Instant */
//                val dayOfWeek = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC"))
//                    .toLocalDate().dayOfWeek
//                return dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY
//            }
//            // 只可選擇2024年以後日期
//            override fun isSelectableYear(year: Int): Boolean {
//                return year >= 2024
//            }
//        }
//    )

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DatePicker(
            state = datePickerState,
            title = { Text(text = "XYZ Hotel") },
            headline = { Text(text = "Check-in Date") }
        )
        // 顯示選取日期(已格式化)
        Text(
            text = "Selected date: ${
                datePickerState.selectedDateMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                        .toLocalDate().format(ofLocalizedDate(FormatStyle.MEDIUM))
                } ?: "no selection"
            }"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    FoodHunterTheme {

        CalendarScreen(){}
    }
}