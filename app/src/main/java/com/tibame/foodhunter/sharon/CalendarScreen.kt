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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.card.NoteOrGroupCard
import com.tibame.foodhunter.sharon.data.Book
import com.tibame.foodhunter.sharon.util.CalendarUiState
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.util.DateUtil
import com.tibame.foodhunter.sharon.viewmodel.BookViewModel
import com.tibame.foodhunter.sharon.viewmodel.CalendarVM
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController = rememberNavController(),
    calendarVM: CalendarVM,
    bookViewModel: BookViewModel = viewModel(), // 書籍 ViewModel

) {
    val uiState by calendarVM.uiState.collectAsState()
    val books by bookViewModel.bookState.collectAsState() // 取得書籍狀態

    // 初始化當天為選中日期
    var selectedDate by remember {
        mutableStateOf<CalendarUiState.Date?>(null)  // 初始化為 null
    }

    // 在 LaunchedEffect 中設置初始日期
    LaunchedEffect(Unit) {
        if (selectedDate == null) {  // 只在 selectedDate 為 null 時設置
            val today = LocalDate.now()
            selectedDate = uiState.dates.find { date ->
                date.dayOfMonth == today.dayOfMonth.toString() &&
                        date.month == today.monthValue &&
                        date.year == today.year
            }
        }
    }

    var selectedBooks by remember {
        mutableStateOf(emptyList<Book>())
    }

    // 當 selectedDate 改變時更新 selectedBooks
    LaunchedEffect(selectedDate, books) {
        selectedBooks = books.filter { book ->
            selectedDate?.let { date ->
                book.date.dayOfMonth.toString() == date.dayOfMonth &&
                        book.date.monthValue == date.month &&
                        book.date.year == date.year
            } ?: false
        }
    }

    Column(
        modifier = Modifier
//            .fillMaxSize()
//            .padding(5.dp)
    ) {
        // 更新日期列表，使得用戶選中的日期顯示選中狀態
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

        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            item {
                NoteOrGroupCard(
                    type = CardContentType.GROUP,
                    date = "10/17",
                    day = "星期四",
                    title = "說好的減肥呢",
                    restaurantName = "麥噹噹",
                    restaurantAddress = "台北市中山區南京東路三段222號",
                    headcount = 4,
                    isPublic = false,
                    onClick = {}
                )
            }

            item {
                NoteOrGroupCard(
                    onClick = {},
                    type = CardContentType.NOTE,
                    date = "10/17",
                    day = "星期四",
                    title = "小巷中的咖啡廳",
                    content = "這裡有各種美味的咖啡和小吃、環境乾淨...",
                    imageResId = R.drawable.sushi_image_1
                )
            }

            item {
                NoteOrGroupCard(
                    type = CardContentType.GROUP,
                    date = "10/17",
                    day = "星期四",
                    title = "說好的減肥呢",
                    restaurantName = "麥噹噹",
                    restaurantAddress = "台北市中山區南京東路三段222號",
                    headcount = 4,
                    isPublic = true,
                    onClick = {}
                )
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
//    val calendarVM: CalendarVM = viewModel() // 取得 ViewModel

//    CalendarScreen()

}

