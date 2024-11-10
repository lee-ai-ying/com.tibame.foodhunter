package com.tibame.foodhunter.sharon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.a871208s.UserViewModel
import com.tibame.foodhunter.sharon.components.card.NoteOrGroupCard
import com.tibame.foodhunter.sharon.data.Book
import com.tibame.foodhunter.sharon.util.CalendarUiState
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Group
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.util.DateUtil
import com.tibame.foodhunter.sharon.viewmodel.BookViewModel
import com.tibame.foodhunter.sharon.viewmodel.CalendarVM
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    calendarVM: CalendarVM,
    userVM: UserViewModel,
) {
    val uiState by calendarVM.uiState.collectAsState()
    val filteredItems by calendarVM.filteredItems.collectAsStateWithLifecycle()
    val isLoading by calendarVM.isLoading.collectAsStateWithLifecycle()
    val memberId by userVM.memberId.collectAsStateWithLifecycle()

    // 初始化資料 取得用戶 ID 並初始化資料
    LaunchedEffect(memberId) {
        calendarVM.initUserData(memberId)
    }

// 選中日期的狀態
    var selectedDate by remember {
        mutableStateOf<CalendarUiState.Date?>(null)
    }

    // 當天要顯示的項目
    var dayItems by remember {
        mutableStateOf<List<Any>>(emptyList())
    }

    // 1. 設置初始日期為當天
    LaunchedEffect(uiState.dates) {
        if (selectedDate == null) {
            val today = LocalDate.now()
            selectedDate = uiState.dates.find { date ->
                date.dayOfMonth == today.dayOfMonth.toString() &&
                        date.month == today.monthValue &&
                        date.year == today.year
            }
        }
    }

    // 2. 當日期被選中時，過濾出該天的項目
    LaunchedEffect(selectedDate, filteredItems) {
        dayItems = filteredItems.filter { item ->
            when (item) {
                is Note -> {
                    selectedDate?.let { date ->
                        val calendar = Calendar.getInstance().apply {
                            time = item.selectedDate
                        }
                        calendar.get(Calendar.DAY_OF_MONTH).toString() == date.dayOfMonth &&
                                (calendar.get(Calendar.MONTH) + 1) == date.month &&
                                calendar.get(Calendar.YEAR) == date.year
                    } ?: false
                }
                else -> false
            }
        }
    }

//    // 當 selectedDate 改變時更新 selectedBooks
//    LaunchedEffect(selectedDate, books) {
//        selectedBooks = books.filter { book ->
//            selectedDate?.let { date ->
//                book.date.dayOfMonth.toString() == date.dayOfMonth &&
//                        book.date.monthValue == date.month &&
//                        book.date.year == date.year
//            } ?: false
//        }
//    }

    Column {
        // 日曆部分
        val updatedDates = uiState.dates.map { date ->
            date.copy(
                isSelected = selectedDate?.let { selected ->
                    date.dayOfMonth == selected.dayOfMonth &&
                            date.month == selected.month &&
                            date.year == selected.year
                } ?: false
            )
        }

        CalendarWidget(
            days = DateUtil.daysOfWeek,
            yearMonth = uiState.yearMonth,
            dates = updatedDates,  // 傳遞更新後的 dates 列表
            // 切換到上一個月份
            onPreviousMonthButtonClicked = { prevMonth ->
                calendarVM.toPreviousMonth(prevMonth)
            },
            // 切換到下一個月份
            onNextMonthButtonClicked = { nextMonth ->
                calendarVM.toNextMonth(nextMonth)
            },
            // 點擊日期時更新選擇的日期和顯示的書籍
            onDateClickListener = { date ->
                selectedDate = date
            },
        )
        // 載入提示
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            when {
                isLoading && filteredItems.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                filteredItems.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("目前沒有資料")
                    }
                }
                // 卡片列表部分
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(
                            count = dayItems.size,
                            key = { index ->
                                when (val item = dayItems[index]) {
                                    is Note -> "note_${item.noteId}"
                                    else -> index.toString()
                                }
                            }
                        ) { index ->
                            when (val item = dayItems[index]) {
                                is Note -> {
                                    NoteOrGroupCard(
                                        onClick = {
                                            navController.navigate("note/edit/${item.noteId}")
                                        },
                                        type = item.type,
                                        date = item.date,
                                        day = item.day,
                                        title = item.title,
                                        content = item.content,
                                        imageResId = item.imageResId,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp)
                                    )
                                }
                                /* 之後加入揪團時解開註解
                                is Group -> {
                                    NoteOrGroupCard(
                                        type = CardContentType.GROUP,
                                        date = item.groupDate,
                                        day = item.day,
                                        title = item.groupName,
                                        restaurantName = item.restaurantName,
                                        restaurantAddress = item.restaurantAddress,
                                        headcount = 4,  // 需要加入到 Group model
                                        isPublic = item.isPublic == 1,
                                        onClick = {}  // 揪團不需要點擊事件
                                    )
                                }
                                */
                                else -> {} // 處理其他可能的情況
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarScreenPreview() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val calendarVM: CalendarVM = viewModel() // 取得 ViewModel
    val userVM: UserViewModel = viewModel() // 取得 ViewModel

    CalendarScreen(navController, calendarVM, userVM)

}

