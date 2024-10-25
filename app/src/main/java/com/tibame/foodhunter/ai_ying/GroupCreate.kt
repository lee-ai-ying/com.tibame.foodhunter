package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDefaults.DatePickerHeadline
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofLocalizedDate
import java.time.format.FormatStyle


@Composable
fun GroupCreate(
    navController: NavHostController,
    gChatVM: GroupViewModel
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectDate by remember {
        mutableStateOf(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(1) {
            //*
            GroupText(text = stringResource(R.string.str_create_group))
            GroupText(text = stringResource(R.string.str_create_name))
            GroupSingleInput()
            GroupText(text = stringResource(R.string.str_create_location))
            GroupSingleWithIcon {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = ""
                )
            }
            GroupText(text = stringResource(R.string.str_create_time))
            GroupSingleInputWithIcon(
                placeholder = {
                    Text(selectDate)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        showDatePickerDialog = true
                    }
                )
            }
            GroupText(text = stringResource(R.string.str_create_price))
            GroupPriceSlider()
//        GroupText(text = stringResource(R.string.str_create_payment))
//        GroupDropDownMenu(listOf("各自支付", "平均分攤", "一人買單"))
            GroupText(text = stringResource(R.string.str_create_member))
            GroupSelectMember()
            GroupText(text = stringResource(R.string.str_create_public))
            GroupDropDownMenu(listOf("公開", "邀請", "私人"))
            GroupText(text = stringResource(R.string.str_create_describe))
            GroupBigInput(5)
            //*/
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(0.2f)
                ) {
                    Text("確定")
                }
            }

        }
    }
//    Box {
//        if (showDatePickerDialog) {
//            MyDatePickerDialog(
//                showData = selectDate,
//                onDismissRequest = {
//                    showDatePickerDialog = false
//                },
//                // 確定時會接收到選取日期
//                onConfirm = { utcTimeMillis ->
//                    selectDate = utcTimeMillis?.let {
//                        Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
//                            .toLocalDate().format(ofLocalizedDate(FormatStyle.MEDIUM))
//                    }
//                    showDatePickerDialog = false
//                },
//                // 設定取消時欲執行內容
//                onDismiss = {
//                    showDatePickerDialog = false
//                }
//            )
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    showData: String,
    onDismissRequest: () -> Unit,
    onConfirm: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        // SelectableDates介面用來限制可選擇的日期與年，
        selectableDates = object : SelectableDates {
            // 設定使用者不可選擇週六、日
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                /* 將使用者選取的時間轉成LocalDate物件後取出星期幾的資訊
                   API 26開始支援Instant */
                val dayOfWeek = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC"))
                    .toLocalDate().dayOfWeek
                return dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY
            }

            // 只可選擇2024年以後日期
            override fun isSelectableYear(year: Int): Boolean {
                return year >= 2024
            }
        }
    )

    DatePickerDialog(
        // 點擊對話視窗外部或back按鈕時呼叫，並非點擊dismissButton時呼叫
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                // 點擊確定按鈕時呼叫onConfirm(Long?)並將選取日期傳入以回饋給原畫面
                onClick = {
                    onConfirm(datePickerState.selectedDateMillis)
                }
            ) {
                Text("OK")
            }
        },
        // 設定取消按鈕
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            title = {
                Text(
                    text = "請選擇日期",
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 24.dp,
                            end = 12.dp,
                            top = 16.dp
                        )
                    )
                )
            },
            headline = {
//                DatePickerHeadline(
//                    selectedDateMillis = datePickerState.selectedDateMillis,
//                            displayMode = datePickerState.displayMode,
//                    dateFormatter = DatePickerDefaults.dateFormatter(),
//                    modifier = Modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, bottom = 12.dp))
//                )
                Text(
                    text = showData,
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 24.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                    )
                )
//                DatePickerDefaults.DatePickerHeadline(
//                    selectedDateMillis = datePickerState.selectedDateMillis,
//                    displayMode = datePickerState.displayMode,
//                    dateFormatter = DatePickerDefaults.dateFormatter(),
//                    modifier = Modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, bottom = 12.dp))
//                )
            },
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GroupCreatePreview() {
    MaterialTheme {
        GroupCreate(rememberNavController(), viewModel())
//        MyDatePickerDialog(
//            onDismissRequest = {
//            },
//            // 確定時會接收到選取日期
//            onConfirm = {
//            },
//            // 設定取消時欲執行內容
//            onDismiss = {
//            }
//        )
    }
}