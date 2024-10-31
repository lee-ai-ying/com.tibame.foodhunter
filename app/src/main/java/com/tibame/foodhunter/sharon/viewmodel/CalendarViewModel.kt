package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.Book
import com.tibame.foodhunter.sharon.data.CalendarDataSource
import com.tibame.foodhunter.sharon.data.CalendarUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    // 搜尋相關
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 篩選相關
    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    fun handleSearch(query: String) {
        _searchQuery.value = query
        // 實作搜尋邏輯
    }

    fun handleFilter(filters: List<String>) {
        _selectedFilters.value = filters
        // 實作篩選邏輯
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun clearFilters() {
        _selectedFilters.value = emptyList()
    }




    private val dataSource by lazy { CalendarDataSource() }

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    // 用於儲存所有書籍資料
    private var allBooks: List<Book> = emptyList()

    init {
        // 初始化日期列表
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    dates = updateCalendarDates(currentState.yearMonth)
                )
            }
        }
    }

    // 更新書籍數據的方法
    fun updateBooks(books: List<Book>) {
        allBooks = books
        viewModelScope.launch {
            // 使用當前月份來更新日期
            _uiState.update { currentState ->
                currentState.copy(
                    dates = updateCalendarDates(uiState.value.yearMonth)
                )
            }
        }
    }

    // 更新日期列表，根據月份和書籍資訊
    private fun updateCalendarDates(yearMonth: YearMonth): List<CalendarUiState.Date> {
        val dates = dataSource.getDates(yearMonth)

        return dates.map { date ->
            date.copy(
                hasBook = allBooks.any { book ->
                    book.date.year == date.year &&
                            book.date.monthValue == date.month &&
                            book.date.dayOfMonth.toString() == date.dayOfMonth
                }
            )
        }
    }

    // 切換到下一個月份
    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth,
                    dates = updateCalendarDates(nextMonth)
                )
            }
        }
    }

    // 切換到上一個月份
    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth,
                    dates = updateCalendarDates(prevMonth)
                )
            }
        }
    }
}







