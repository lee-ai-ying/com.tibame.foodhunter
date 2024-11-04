package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.Book
import com.tibame.foodhunter.sharon.util.CalendarDataSource
import com.tibame.foodhunter.sharon.util.CalendarUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth


data class CardItem(val id: String, val title: String, val description: String)

class CalendarViewModel : TopBarViewModel() {
    // 儲存選中的篩選條件（新增）
    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters: StateFlow<List<String>> = _selectedFilters.asStateFlow()

    // 儲存所有日曆卡片項目（所有原始數據）
    private val _allCalendarItems = MutableStateFlow<List<CardItem>>(emptyList())
    // 用於顯示的日曆卡片項目（過濾後的結果）
    private val _calendarItems = MutableStateFlow<List<CardItem>>(emptyList())
    val calendarItems: StateFlow<List<CardItem>> = _calendarItems.asStateFlow()

    init {
        // 初始化一些測試資料
        loadTestData()
    }

    private fun loadTestData() {
        val testItems = listOf(
            CardItem(
                id = "1",
                title = "早午餐約會",
                description = "好吃的早午餐"
            ),
            CardItem(
                id = "2",
                title = "下午茶",
                description = "甜點時間"
            ),
            CardItem(
                id = "3",
                title = "聚餐",
                description = "與朋友的聚餐"
            )
        )
        _allCalendarItems.value = testItems
        _calendarItems.value = testItems // 初始化顯示所有項目
    }

    // 切換搜尋框時也重置篩選條件（重寫父類的方法以適應特定邏輯）
    override fun toggleSearchVisibility() {
        super.toggleSearchVisibility()
        if (!_isSearchVisible.value) {
            // 如果關閉搜尋，同時隱藏篩選 Chip 並清空已選篩選條件
            _isFilterChipVisible.value = false
            _selectedFilters.value = emptyList()
            // 重置日曆項目顯示
            _calendarItems.value = _allCalendarItems.value
        }
    }

    // 更新選擇的篩選條件
    fun updateSelectedFilter(filter: String) {
        val currentFilters = _selectedFilters.value.toMutableList()
        if (currentFilters.contains(filter)) {
            currentFilters.remove(filter)
        } else {
            currentFilters.add(filter)
        }
        _selectedFilters.value = currentFilters

        // 更新日曆項目顯示
        filterItems()
    }

    // 搜尋文字變更時的處理邏輯
    override fun updateSearchQuery(newQuery: String) {
        super.updateSearchQuery(newQuery)
        // 更新日曆項目顯示
        filterItems()
    }

    // 統一的過濾日曆項目的方法，根據搜尋和篩選條件進行過濾
    private fun filterItems() {
        val query = _searchQuery.value
        val filters = _selectedFilters.value

        // 開始過濾邏輯
        viewModelScope.launch {
            var filteredItems = _allCalendarItems.value

            // 根據搜尋關鍵字進行過濾
            if (query.isNotEmpty()) {
                filteredItems = filteredItems.filter { item ->
                    item.title.contains(query, ignoreCase = true) ||
                            item.description.contains(query, ignoreCase = true)
                }
            }

            // 根據篩選條件進行過濾
            if (filters.isNotEmpty()) {
                filteredItems = filteredItems.filter { item ->
                    // 假設每個篩選條件與 item 的 title 關聯
                    filters.any { filter -> item.title.contains(filter, ignoreCase = true) }
                }
            }

            // 更新過濾後的結果
            _calendarItems.value = filteredItems
        }
    }

    // 處理篩選邏輯的應用（傳入新的篩選條件列表）
    fun applyFilter(filters: List<String>) {
        _selectedFilters.value = filters
        // 更新日曆項目顯示
        filterItems()
    }
}
class CalendarViewMode1:ViewModel() {


    // 加載所有日曆項目
    private fun loadAllCalendarItems() {
        viewModelScope.launch {
            // 假設從某個資料來源取得所有項目
//            _calendarItems.value = dataSource.getAllCalendarItems()
        }
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



